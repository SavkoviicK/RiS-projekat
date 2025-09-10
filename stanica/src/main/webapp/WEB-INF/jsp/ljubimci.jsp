<%@ page contentType="text/html; charset=UTF-8" %>
<% String ctx = request.getContextPath(); %>
<!DOCTYPE html>
<html lang="sr">
<head>
  <meta charset="UTF-8">
  <title>Ljubimci</title>
  <style>
    body{font-family:system-ui,Arial;margin:0;background:#f7f9fb}
    header{display:flex;align-items:center;justify-content:space-between;gap:16px;
           padding:16px 24px;background:#fff;border-bottom:1px solid #e6e6e6}
    .wrap{max-width:900px;margin:24px auto;padding:0 16px}
    nav a{margin-right:12px;text-decoration:none;color:#0b5ed7}
    table{width:100%;border-collapse:collapse;background:#fff;border:1px solid #e6e6e6}
    th,td{padding:10px;border-top:1px solid #eee;text-align:left}
    thead tr{background:#f2f4f7}
    .btn{background:#0b5ed7;color:#fff;border:none;border-radius:8px;cursor:pointer}
    .btn-del{background:#dc2626;color:#fff;border:none;border-radius:8px;cursor:pointer;padding:6px 10px}
    .btn-del:hover{filter:brightness(1.05)}

    .no-access{
      display:none;background:#fff;border:1px solid #e6e6e6;border-radius:12px;
      padding:32px;text-align:center
    }
    .no-access img{max-width:220px;height:auto;display:block;margin:0 auto 16px}
    .no-access h2{margin:8px 0 6px}
    .no-access p{color:#666;margin:0}
  </style>
</head>
<body>
<header>
  <div style="display:flex;align-items:center;gap:12px">
    <img src="<%=ctx%>/img/logo.png" alt="Logo" style="height:46px">
    <strong>Veterinarska stanica Vaš ljubimac</strong>
  </div>
  <nav>
    <a href="<%=ctx%>/">Početna</a>
    <a href="<%=ctx%>/ljubimci">Ljubimci</a>
    <a href="<%=ctx%>/pregledi">Pregledi</a>
    <a href="<%=ctx%>/poruke">Poruke</a>
    <a id="navIzvestaji" href="<%=ctx%>/izvestaji" style="display:none">Izveštaji</a>
  </nav>
  <div id="userBox"><span id="userInfo"></span>
    <button id="btnLogout" style="display:none;margin-left:8px" class="btn">Odjava</button>
  </div>
</header>

<div class="wrap">
  <h1>🐾 Ljubimci</h1>

  <div id="noAccess" class="no-access">
    <img src="<%=ctx%>/img/tuzna.png" alt="Nema pristup">
    <h2>Nemate ovlašćenje za ovu stranicu</h2>
    <p>Ova sekcija je dostupna samo vlasnicima.</p>
  </div>

  <!-- poruke i tabela -->
  <div id="msg" style="margin:8px 0 16px 0;color:#444"></div>

  <div id="ownerPanel" style="display:none">
    <div id="tblWrap">
      <table id="tbl">
        <thead>
          <tr>
            <th>ID</th>
            <th>Ime</th>
            <th>Vrsta</th>
            <th>Rasa</th>
            <th>Pol</th>
            <th>Vlasnik (email)</th>
            <th>Napomena</th>
            <th>Akcije</th>
          </tr>
        </thead>
        <tbody id="rows">
          <tr><td colspan="8">Učitavam…</td></tr>
        </tbody>
      </table>
    </div>

    <hr style="margin:24px 0">

    <!-- samo VLASNIK vidi ovu karticu -->
    <div id="addPetCard">
      <h2 style="margin:0 0 12px 0">➕ Dodaj ljubimca</h2>
      <form id="frmPet" onsubmit="return addPet(event)" style="display:grid;grid-template-columns:repeat(3,1fr);gap:12px;background:#fff;border:1px solid #e6e6e6;border-radius:8px;padding:12px">
        <label>Ime
          <input name="ime" required style="width:100%;padding:8px">
        </label>
        <label>Vrsta
          <input name="vrsta" required style="width:100%;padding:8px">
        </label>
        <label>Rasa
          <input name="rasa" style="width:100%;padding:8px">
        </label>
        <label>Pol
          <select name="pol" required style="width:100%;padding:8px">
            <option value="M">M</option>
            <option value="Ž">Ž</option>
          </select>
        </label>
        <label style="grid-column:1 / -1">Napomena
          <input name="napomena" style="width:100%;padding:8px">
        </label>
        <div style="grid-column:1 / -1;text-align:right">
          <button class="btn" type="submit" style="padding:10px 16px">Sačuvaj</button>
        </div>
      </form>
    </div>
  </div>
</div>

<script src="<%=ctx%>/js/auth.js"></script>
<script>
  const BASE = '<%=ctx%>';

  // header
  if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', paintUserInfoHeader);
  } else {
    paintUserInfoHeader();
  }
  try { if (typeof showAdminMenu==='function') showAdminMenu(); } catch(e){}

  // uloga: vlasnik
  function isOwnerRole(roles){
    return roles.some(r => {
      const v = typeof r === 'string' ? r : (r.authority || r.role || '');
      const up = String(v || '').toUpperCase();
      return up.includes('ROLE_VLASNIK') || up === 'VLASNIK';
    });
  }

  async function loadPets(){
    const msg  = document.getElementById('msg');
    const rows = document.getElementById('rows');
    const tblWrap = document.getElementById('tblWrap');

    if (!localStorage.getItem('jwt')){
      msg.innerHTML = 'Niste prijavljeni. <a href="'+BASE+'/prijava">Prijavite se</a> da biste videli svoje ljubimce.';
      tblWrap.style.display = 'none';
      return;
    }

    rows.innerHTML = '<tr><td colspan="8">Učitavam…</td></tr>';
    tblWrap.style.display = 'block';

    try{
      const data = await fetchJSON(`${BASE}/api/ljubimci`, { headers: authHeader() }, 'msg', null, { appendServerText:false });
      if (!Array.isArray(data) || data.length === 0){
        rows.innerHTML = '<tr><td colspan="8">Nema podataka.</td></tr>';
        showMsg('msg', '');
        return;
      }

      rows.innerHTML = data.map(p => (
        '<tr>'
          + '<td>' + (p.id || '') + '</td>'
          + '<td>' + (p.ime || '') + '</td>'
          + '<td>' + (p.vrsta || '') + '</td>'
          + '<td>' + (p.rasa || '') + '</td>'
          + '<td>' + (p.pol || '') + '</td>'
          + '<td>' + (p.vlasnikEmail || '') + '</td>'
          + '<td>' + (p.napomena || '') + '</td>'
          + '<td><button class="btn-del" data-id="'+(p.id||'')+'">🗑 Obriši</button></td>'
        + '</tr>'
      )).join('');
      showMsg('msg', '');
    }catch(_){
      tblWrap.style.display = 'none';
    }
  }

  async function addPet(e){
    e.preventDefault();
    const f = document.getElementById('frmPet');
    const body = {
      ime: f.ime.value.trim(),
      vrsta: f.vrsta.value.trim(),
      rasa:  f.rasa.value.trim() || null,
      pol:   f.pol.value,
      napomena: f.napomena.value.trim() || null
    };
    try{
      await fetchJSON(`${BASE}/api/ljubimci`, {
        method: 'POST',
        headers: { ...authHeader(), 'Content-Type': 'application/json' },
        body: JSON.stringify(body)
      }, 'msg', null, { appendServerText:false });
      f.reset();
      await loadPets();
    }catch(_){}
    return false;
  }

  document.addEventListener('click', async (e)=>{
    const btn = e.target.closest('.btn-del');
    if (!btn) return;
    const id = btn.getAttribute('data-id');
    if (!id) return;

    if (!confirm('Obrisati ljubimca #' + id + '?')) return;

    try{
      const res = await fetch(`${BASE}/api/ljubimci/` + encodeURIComponent(id), {
        method: 'DELETE',
        headers: authHeader()
      });
      if (res.status !== 204){
        const txt = await res.text();
        const msg = friendlyHttpMessage(res) + (txt ? (' – ' + txt) : '');
        throw new Error(msg);
      }
      showMsg('msg', 'Ljubimac obrisan ✅');
      await loadPets();
    }catch(err){
      showMsg('msg', err.message || 'Greška pri brisanju.');
    }
  });

  window.addEventListener('load', () => {
    try {
      const raw = JSON.parse(localStorage.getItem('roles')||'[]');
      const roles = Array.isArray(raw) ? raw : (raw.roles || raw.authorities || []);
      const isOwner = isOwnerRole(roles);

      const ownerPanel = document.getElementById('ownerPanel');
      const noAccess = document.getElementById('noAccess');
      const addCard = document.getElementById('addPetCard');

      if (isOwner) {
        ownerPanel.style.display = 'block';
        noAccess.style.display = 'none';
        if (addCard) addCard.style.display = '';
        loadPets();
      } else {
        ownerPanel.style.display = 'none';
        noAccess.style.display = 'block';
        if (addCard) addCard.style.display = 'none';
      }
    } catch(e){
      console.error(e);
    }
  });
</script>
</body>
</html>
