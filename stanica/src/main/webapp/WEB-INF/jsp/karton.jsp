<%@ page contentType="text/html; charset=UTF-8" isELIgnored="true" %>
<!DOCTYPE html>
<html lang="sr">
<head>
  <meta charset="UTF-8">
  <title>Karton ljubimca</title>
  <style>
    body{font-family:system-ui,Arial;margin:0;background:#f7f9fb;color:#111}
    header{display:flex;align-items:center;justify-content:space-between;gap:16px;
           padding:16px 24px;background:#fff;border-bottom:1px solid #e6e6e6}
    nav a{margin-right:12px;text-decoration:none;color:#0b5ed7}
    .wrap{max-width:1000px;margin:24px auto;padding:0 16px}
    .muted{color:#6b7280}
    .card{background:#fff;border:1px solid #e6e6e6;border-radius:12px;padding:16px;margin:14px 0}
    .row{display:flex;gap:12px;align-items:center;flex-wrap:wrap}
    button{background:#0b5ed7;color:#fff;border:none;border-radius:8px;padding:8px 12px;cursor:pointer}
    table{width:100%;border-collapse:collapse;background:#fff;border:1px solid #e6e6e6}
    th,td{padding:10px;border-top:1px solid #eee;text-align:left;vertical-align:top}
    thead tr{background:#f2f4f7}
    .no-access{display:none;background:#fff;border:1px solid #e6e6e6;border-radius:12px;padding:32px;text-align:center}
    .no-access img{max-width:220px;height:auto;display:block;margin:0 auto 12px}
    @media print{
      header, .actions, .back { display:none !important; }
      body{background:#fff}
      .wrap{margin:0;padding:0}
      .card{border:none;margin:0;padding:0}
      table{border:1px solid #000}
      th,td{border-top:1px solid #000}
    }
  </style>
</head>
<body>
<header>
  <div style="display:flex;align-items:center;gap:12px">
    <img src="/img/logo.png" alt="Logo" style="height:46px">
    <strong>Veterinarska stanica</strong>
  </div>
  <nav>
    <a href="/">Početna</a>
    <a href="/ljubimci">Ljubimci</a>
    <a href="/pregledi">Pregledi</a>
    <a href="/poruke">Poruke</a>
    <a id="navIzvestaji" href="/izvestaji" style="display:none">Izveštaji</a>
  </nav>
  <div id="userBox"><span id="userInfo"></span>
    <button id="btnLogout" style="display:none;margin-left:8px">Odjava</button>
  </div>
</header>

<div class="wrap">
  <a class="back" href="/pregledi" style="text-decoration:none">&larr; Nazad na preglede</a>
  <h1 id="title">🩺 Karton ljubimca</h1>
  <div id="msg" class="muted" style="margin:6px 0 14px"></div>

  <div id="noAccess" class="no-access">
    <img src="/img/tuzna.png" alt="Nema pristup">
    <h2>Morate biti prijavljeni kao VETERINAR</h2>
    <p>Prijavite se da biste videli karton ljubimca.</p>
    <a class="btn" href="/prijava">Prijava</a>
  </div>

  <div id="content" style="display:none">
    <div class="card actions" style="display:flex;justify-content:space-between;align-items:center">
      <div class="muted">Karton za: <strong id="petName">—</strong></div>
      <div class="row">
        <button id="btnPrint">Štampaj / Sačuvaj kao PDF</button>
      </div>
    </div>

    <!-- META: samo Ljubimac i Vlasnik -->
    <div class="card" id="metaCard" style="display:none">
      <div class="row">
        <div><strong>Ljubimac:</strong> <span id="m_pet">—</span></div>
        <div><strong>Vlasnik:</strong> <span id="m_owner">—</span></div>
      </div>
    </div>

    <h2 style="margin:16px 0 8px">📄 Unosi</h2>
    <table id="tbl">
      <thead>
        <tr>
          <th>#</th>
          <th>Datum</th>
          <th>Dijagnoza</th>
          <th>Terapija</th>
          <th>Uneo</th>
        </tr>
      </thead>
      <tbody id="rows">
        <tr><td colspan="5">Učitavam…</td></tr>
      </tbody>
    </table>
  </div>
</div>

<script src="/js/auth.js"></script>
<script>
  if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', paintUserInfoHeader);
  } else {
    paintUserInfoHeader();
  }

  function qp(name){ const u=new URL(window.location.href); return u.searchParams.get(name); }
  function safe(v){ return (v===null||v===undefined)?'':String(v); }
  function show(id,on){ const el=document.getElementById(id); if(el) el.style.display = on?'':'none'; }
  function setTxt(id,txt){ const el=document.getElementById(id); if(el) el.textContent = txt; }

  async function fetchJSON(url,opts,msgId){
    const res = await fetch(url,opts||{});
    if(!res.ok){
      let t=''; try{ t=await res.text(); }catch(_){}
      const m=t||('Greška ('+res.status+')'); if(msgId) setTxt(msgId,m); throw new Error(m);
    }
    const ct = res.headers.get('content-type')||''; 
    return ct.includes('application/json') ? res.json() : res.text();
  }

  function requireVetRole(){
    try{
      const roles=(JSON.parse(localStorage.getItem('roles')||'[]')||[]).map(r=>r.authority||r||'');
      return roles.includes('ROLE_VETERINAR') || roles.includes('VETERINAR');
    }catch{ return false; }
  }

  function renderTable(list){
    const tb=document.getElementById('rows');
    if(!Array.isArray(list)||!list.length){
      tb.innerHTML='<tr><td colspan="5">Nema zapisa.</td></tr>';
      return;
    }
    // zaglavlje (pet + vlasnik ako postoji u DTO-u)
    setTxt('petName', list[0].ljubimacIme || '—');
    setTxt('m_pet', list[0].ljubimacIme || '—');
    setTxt('m_owner', (list[0].vlasnikIme ? (list[0].vlasnikIme + ' ' + (list[0].vlasnikPrezime||'')) : '—'));
    show('metaCard', true);

    tb.innerHTML = list.map((x,i)=>{
      const datum = safe(x.datum);
      const dij   = safe(x.dijagnoza);
      const ter   = safe(x.terapija);
      const uneo  = safe(x.veterinarIme); // kolona ostaje
      return `<tr>
        <td>${i+1}</td>
        <td>${datum}</td>
        <td>${dij}</td>
        <td>${ter}</td>
        <td>${uneo}</td>
      </tr>`;
    }).join('');
  }

  window.addEventListener('load', async ()=>{
    const token=localStorage.getItem('jwt');
    const isVet=requireVetRole();
    const ljubimacId=qp('ljubimacId');

    if(!token || !isVet){ show('noAccess',true); show('content',false); setTxt('msg','Niste prijavljeni kao veterinar.'); return; }
    if(!ljubimacId){ show('content',false); setTxt('msg','Nedostaje parametar ?ljubimacId='); return; }

    show('noAccess',false); show('content',true); setTxt('msg','Učitavam karton...');
    try{
      const data = await fetchJSON(`/api/medicinski-zapisi/karton/${encodeURIComponent(ljubimacId)}`, { headers: authHeader() }, 'msg');
      renderTable(Array.isArray(data)?data:[]);
      setTxt('msg','');
    }catch(e){
      setTxt('msg', e.message || 'Greška pri učitavanju kartona.');
      document.getElementById('rows').innerHTML='<tr><td colspan="5">Greška.</td></tr>';
    }
  });

  document.getElementById('btnPrint')?.addEventListener('click', ()=>window.print());
</script>
</body>
</html>
