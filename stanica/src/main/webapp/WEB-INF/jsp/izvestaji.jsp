<%@ page contentType="text/html; charset=UTF-8" %>
<% String ctx = request.getContextPath(); %>
<!DOCTYPE html>
<html lang="sr">
<head>
  <meta charset="UTF-8">
  <title>Izveštaji</title>
  <style>
    body{font-family:system-ui,Arial;margin:0;background:#f7f9fb}
    header{display:flex;align-items:center;justify-content:space-between;gap:16px;
           padding:16px 24px;background:#fff;border-bottom:1px solid #e6e6e6}
    .wrap{max-width:1100px;margin:24px auto;padding:0 16px}
    nav a{margin-right:12px;text-decoration:none;color:#0b5ed7}
    .card{background:#fff;border:1px solid #e6e6e6;border-radius:12px;padding:16px}
    button{background:#0b5ed7;color:#fff;border:none;border-radius:8px;padding:8px 12px;cursor:pointer}
    button:disabled{opacity:.6;cursor:not-allowed}
    .muted{color:#666}
    iframe{width:100%;height:70vh;border:1px solid #e6e6e6;border-radius:8px;background:#fff}

    /* NO ACCESS blok */
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
    <strong>Veterinarska stanica</strong>
  </div>
  <nav>
    <a href="<%=ctx%>/">Početna</a>
    <a href="<%=ctx%>/ljubimci">Ljubimci</a>
    <a href="<%=ctx%>/pregledi">Pregledi</a>
    <a href="<%=ctx%>/poruke">Poruke</a>
    <a id="navIzvestaji" href="<%=ctx%>/izvestaji" style="display:none">Izveštaji</a>
  </nav>
  <div id="userBox"><span id="userInfo"></span>
    <button id="btnLogout" style="display:none;margin-left:8px">Odjava</button>
  </div>
</header>

<div class="wrap">
  <h1>📄 Izveštaji</h1>
  <p id="msg" class="muted">Samo ADMIN može da preuzme izveštaje.</p>

  <div id="noAccess" class="no-access">
    <img src="<%=ctx%>/img/tuzna.png" alt="Nema pristup">
    <h2>Nemate ovlašćenje za ovu stranicu</h2>
    <p>Kontaktirajte administratora ili se vratite na početnu.</p>
  </div>

  <div id="adminPanel" class="card" style="display:none">
    <div style="display:flex;gap:12px;flex-wrap:wrap;margin-bottom:12px">
      <button id="btnPoDanu">Pregledi po danu (PDF)</button>
      <button id="btnPoVet">Pregledi po veterinaru (PDF)</button>
    </div>
    <iframe id="pdf" style="display:none"></iframe>
  </div>
</div>

<script src="<%=ctx%>/js/auth.js"></script>
<script>
document.addEventListener('DOMContentLoaded', () => {
  const BASE = '<%=ctx%>';

  if (typeof paintUserInfoHeader === 'function') paintUserInfoHeader();
  if (typeof showAdminMenu === 'function') showAdminMenu();

  const raw = JSON.parse(localStorage.getItem('roles') || '[]');
  const roles = Array.isArray(raw) ? raw : (raw.roles || raw.authorities || []);
  const isAdmin = roles.some(r => {
    const v = typeof r === 'string' ? r : (r.authority || r.role || '');
    const up = String(v || '').toUpperCase();
    return up.includes('ROLE_ADMIN') || up === 'ADMIN';
  });

  const adminPanel = document.getElementById('adminPanel');
  const msg = document.getElementById('msg');
  const iframe = document.getElementById('pdf');
  const noAccess = document.getElementById('noAccess');

  if (isAdmin) {
    adminPanel.style.display = 'block';
    noAccess.style.display = 'none';
    msg.textContent = 'Kliknite na neko dugme da prikažete PDF izveštaj.';
  } else {
    adminPanel.style.display = 'none';
    noAccess.style.display = 'block';
    msg.textContent = ' ';
  }

  async function fetchPdf(url){
    const res = await fetch(url, {
      headers: { ...authHeader(), 'Accept': 'application/pdf' },
      cache: 'no-store'
    });
    if(!res.ok){
      let txt = '';
      try { txt = await res.text(); } catch(_) {}
      throw new Error('Greška ' + res.status + (txt?': '+txt:''));
    }
    return await res.blob();
  }

  let currentUrl = null;
  function showPdf(blob){
    if (currentUrl) URL.revokeObjectURL(currentUrl);
    currentUrl = URL.createObjectURL(blob);
    iframe.src = currentUrl;
    iframe.style.display = 'block';
  }

  const btnPoDanu = document.getElementById('btnPoDanu');
  const btnPoVet  = document.getElementById('btnPoVet');

  if (isAdmin && btnPoDanu) btnPoDanu.onclick = async ()=>{
    msg.textContent = 'Učitavam PDF...';
    try{
      const blob = await fetchPdf(`${BASE}/api/izvestaji/pregledi-po-danu.pdf`);
      showPdf(blob);
      msg.textContent = 'Izveštaj učitan.';
    }catch(e){
      msg.textContent = e.message;
      iframe.style.display='none';
    }
  };

  if (isAdmin && btnPoVet) btnPoVet.onclick = async ()=>{
    msg.textContent = 'Učitavam PDF...';
    try{
      const blob = await fetchPdf(`${BASE}/api/izvestaji/pregledi-po-veterinaru.pdf`);
      showPdf(blob);
      msg.textContent = 'Izveštaj učitan.';
    }catch(e){
      msg.textContent = e.message;
      iframe.style.display='none';
    }
  };
});
</script>
</body>
</html>
