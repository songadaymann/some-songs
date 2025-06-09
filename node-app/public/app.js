let user = null;
let songsCache = [];
let playlists = [];
let provider = null;

async function api(path, opts) {
  const res = await fetch(path, opts);
  if (!res.ok) throw new Error(await res.text());
  return res.json();
}

function show(view) {
  document.querySelectorAll('.view').forEach(v => v.classList.add('hidden'));
  document.getElementById(view).classList.remove('hidden');
}

async function loadSongs() {
  songsCache = await api('/songs');
  const container = document.getElementById('songs');
  container.innerHTML = '';
  songsCache.forEach(song => {
    const div = document.createElement('div');
    div.className = 'song';
    div.dataset.id = song.token_id;
    const sale = song.forSale ? '<span class="material-icons sale" title="For sale">sell</span>' : '';
    div.innerHTML = `<h3>${song.name} ${sale}</h3>
      <audio controls src="/songs/${song.token_id}/play"></audio>
      <div>
        <button data-rate="good">👍</button>
        <button data-rate="okay">👌</button>
        <button data-rate="bad">👎</button>
        <span class="avg">${song.rating_avg ? song.rating_avg.toFixed(2) : ''}</span>
        <button class="fav">❤️</button>
        <select class="addpl"><option value="">Add to playlist</option></select>
      </div>
      <div class="comments"></div>
      <input class="commentText" placeholder="Comment">
      <button class="addComment">Send</button>`;
    const audio = div.querySelector('audio');
    audio.addEventListener('ended', () => {
      api(`/songs/${song.token_id}/listen`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ userId: user.id, elapsed: audio.duration })
      });
    });
    div.querySelectorAll('button[data-rate]').forEach(btn => {
      btn.onclick = async () => {
        await api(`/songs/${song.token_id}/rate`, {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ userId: user.id, rating: btn.dataset.rate })
        });
        loadSongs();
      };
    });
    div.querySelector('.fav').onclick = async () => {
      await api(`/users/${user.id}/favorites`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ songId: song.token_id })
      });
      loadFavorites();
    };
    div.querySelector('.addComment').onclick = async () => {
      const text = div.querySelector('.commentText').value.trim();
      if (!text) return;
      await api(`/songs/${song.token_id}/comments`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ userId: user.id, text })
      });
      div.querySelector('.commentText').value = '';
      loadSongComments(song.token_id, div.querySelector('.comments'));
    };
    loadSongComments(song.token_id, div.querySelector('.comments'));
    container.appendChild(div);
  });
  populatePlaylistOptions();
}

async function loadSongComments(id, el) {
  const comments = await api(`/songs/${id}/comments`);
  el.innerHTML = comments.map(c => `<div>${c.text}</div>`).join('');
}

async function loadPlaylists() {
  playlists = await api('/playlists');
  const container = document.getElementById('playlists');
  container.innerHTML = `<h2>Your Playlists</h2>
    <div id="plList"></div>
    <input id="plName" placeholder="New playlist">
    <button id="plCreate">Create</button>`;
  const listDiv = container.querySelector('#plList');
  playlists.filter(p => p.userId === user.id).forEach(pl => {
    const d = document.createElement('div');
    d.innerHTML = `<strong>${pl.name}</strong> (${pl.songIds.length}) <button data-pl="${pl.id}">View</button>`;
    d.querySelector('button').onclick = () => showPlaylist(pl.id);
    listDiv.appendChild(d);
  });
  container.querySelector('#plCreate').onclick = async () => {
    const name = container.querySelector('#plName').value.trim();
    if (!name) return;
    await api('/playlists', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ userId: user.id, name })
    });
    loadPlaylists();
  };
  populatePlaylistOptions();
}

function populatePlaylistOptions() {
  document.querySelectorAll('.addpl').forEach(sel => {
    sel.innerHTML = '<option value="">Add to playlist</option>' +
      playlists.filter(p => p.userId === user.id).map(p => `<option value="${p.id}">${p.name}</option>`).join('');
    sel.onchange = async () => {
      const pid = sel.value;
      if (!pid) return;
      await api(`/playlists/${pid}/add`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ userId: user.id, songId: sel.closest('.song').dataset.id })
      });
      sel.value = '';
    };
  });
}

async function loadFavorites() {
  const favs = await api(`/users/${user.id}/favorites`);
  const container = document.getElementById('favorites');
  container.innerHTML = '<h2>Favorites</h2>';
  favs.forEach(id => {
    const song = songsCache.find(s => String(s.token_id) === String(id));
    if (song) {
      const d = document.createElement('div');
      d.textContent = song.name;
      container.appendChild(d);
    }
  });
}

async function showPlaylist(id) {
  const pl = await api(`/playlists/${id}`);
  const container = document.getElementById('playlists');
  container.innerHTML = `<h2>${pl.name}</h2><button id="back">Back</button><ul id="plSongs"></ul>
    <div class="plComments"></div>
    <input id="plComment" placeholder="Comment"><button id="plSend">Send</button>`;
  container.querySelector('#back').onclick = loadPlaylists;
  const ul = container.querySelector('#plSongs');
  pl.songIds.forEach(id => {
    const li = document.createElement('li');
    const song = songsCache.find(s => String(s.token_id) === String(id));
    li.textContent = song ? song.name : id;
    ul.appendChild(li);
  });
  loadPlaylistComments(id, container.querySelector('.plComments'));
  container.querySelector('#plSend').onclick = async () => {
    const text = container.querySelector('#plComment').value.trim();
    if (!text) return;
    await api(`/playlists/${id}/comments`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ userId: user.id, text })
    });
    container.querySelector('#plComment').value = '';
    loadPlaylistComments(id, container.querySelector('.plComments'));
  };
  show('playlists');
}

async function loadPlaylistComments(id, el) {
  const comments = await api(`/playlists/${id}/comments`);
  el.innerHTML = comments.map(c => `<div>${c.text}</div>`).join('');
}

async function loadBoard() {
  const posts = await api('/board');
  const container = document.getElementById('board');
  container.innerHTML = `<h2>Board</h2>
    <textarea id="postText"></textarea>
    <button id="postBtn">Post</button>
    <div id="posts"></div>`;
  container.querySelector('#postBtn').onclick = async () => {
    const text = container.querySelector('#postText').value.trim();
    if (!text) return;
    await api('/board', { method:'POST', headers:{'Content-Type':'application/json'}, body: JSON.stringify({ userId: user.id, text })});
    loadBoard();
  };
  const postsDiv = container.querySelector('#posts');
  posts.forEach(p => {
    const d = document.createElement('div');
    d.textContent = p.text;
    postsDiv.appendChild(d);
  });
}

async function loadLeaderboard() {
  const users = await api('/users/leaderboard');
  const container = document.getElementById('leaderboard');
  container.innerHTML = '<h2>Leaderboard</h2>';
  users.forEach(u => {
    const d = document.createElement('div');
    d.textContent = `${u.name}: ${u.points}`;
    container.appendChild(d);
  });
}

async function loadCollection() {
  if (!user.wallet) {
    document.getElementById('collection').innerHTML = '<p>No wallet connected</p>';
    return;
  }
  const tokens = await api(`/wallet/collection/${user.wallet}`);
  const container = document.getElementById('collection');
  container.innerHTML = '<h2>Your Collection</h2>';
  tokens.forEach(id => {
    const song = songsCache.find(s => String(s.token_id) === String(id));
    const div = document.createElement('div');
    div.textContent = song ? song.name : id;
    container.appendChild(div);
  });
}

async function startApp() {
  document.getElementById('login').classList.add('hidden');
  document.getElementById('nav').classList.remove('hidden');
  document.querySelectorAll('nav a').forEach(a => a.onclick = () => { show(a.dataset.view); });
  await loadSongs();
  loadPlaylists();
  loadFavorites();
  loadBoard();
  loadLeaderboard();
  loadCollection();
}

window.addEventListener('load', async () => {
  const id = localStorage.getItem('userId');
  if (id) {
    try {
      user = await api(`/users/${id}`);
      startApp();
    } catch {
      localStorage.removeItem('userId');
    }
  }
  if (!user) {
    document.getElementById('register').onclick = async () => {
      const name = document.getElementById('name').value.trim();
      if (!name) return;
      user = await api('/users', { method:'POST', headers:{'Content-Type':'application/json'}, body: JSON.stringify({ name }) });
      localStorage.setItem('userId', user.id);
      startApp();
    };
    if (window.ethereum) {
      document.getElementById('walletLogin').onclick = async () => {
        provider = new ethers.BrowserProvider(window.ethereum);
        await provider.send('eth_requestAccounts', []);
        const signer = await provider.getSigner();
        const address = await signer.getAddress();
        const signature = await signer.signMessage('Login to Some Songs');
        user = await api('/wallet/login', { method:'POST', headers:{'Content-Type':'application/json'}, body: JSON.stringify({ address, signature }) });
        localStorage.setItem('userId', user.id);
        startApp();
      };
    }
  }
});
