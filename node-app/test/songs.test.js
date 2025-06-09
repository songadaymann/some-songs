import request from 'supertest';
import express from 'express';
import { jest } from '@jest/globals';
import songsRouter from '../routes/songs.js';
import playlistsRouter from '../routes/playlists.js';
import boardRouter from '../routes/board.js';
import usersRouter from '../routes/users.js';
import walletRouter from '../routes/wallet.js';
import adminRouter from '../routes/admin.js';
import db from '../lib/db.js';

const app = express();
app.use(express.json());
app.locals.songsCache = [
  { token_id: 1, name: 'Song1' },
  { token_id: 2, name: 'Song2' }
];
app.use('/songs', songsRouter);
app.use('/playlists', playlistsRouter);
app.use('/board', boardRouter);
app.use('/users', usersRouter);
app.use('/wallet', walletRouter);
app.use('/admin', adminRouter);

beforeEach(() => {
  db.data.boardPosts = [];
  db.data.users = [];
  db.data.playlists = [];
  db.data.ratings = [];
  db.data.comments = [];
  db.data.listens = [];
  db.data.brokenReports = [];
  db.write();
  process.env.ALCHEMY_API_KEY = 'test';
  process.env.CONTRACT_ADDRESS = '0x123';
  process.env.ADMIN_KEY = 'adm';
});

describe('GET /songs', () => {
  it('should list songs', async () => {
    const res = await request(app).get('/songs');
    expect(res.statusCode).toBe(200);
    expect(res.body.length).toBe(2);
  });
});

describe('POST /songs/:id/rate', () => {
  it('should reject invalid rating', async () => {
    const res = await request(app).post('/songs/1/rate').send({ userId: 'u1', rating: 'badg' });
    expect(res.statusCode).toBe(400);
  });
});

describe('POST /songs/:id/comments', () => {
  it('should add comment', async () => {
    const res = await request(app).post('/songs/1/comments').send({ userId: 'u1', text: 'hi' });
    expect(res.statusCode).toBe(201);
    expect(res.body.text).toBe('hi');
  });
});

describe('POST /songs/:id/listen', () => {
  it('should record listen', async () => {
    const res = await request(app).post('/songs/1/listen').send({ userId: 'u1' });
    expect(res.statusCode).toBe(201);
  });
});

describe('POST /songs/:id/broken', () => {
  it('should report broken link', async () => {
    const res = await request(app).post('/songs/1/broken').send({ userId: 'u1', note: 'bad link' });
    expect(res.statusCode).toBe(201);
  });
});

describe('playlist comments', () => {
  it('should add and fetch playlist comment', async () => {
    const create = await request(app).post('/playlists').send({ userId: 'u1', name: 'P1', songIds: [] });
    expect(create.statusCode).toBe(201);
    const pid = create.body.id;
    const cRes = await request(app).post(`/playlists/${pid}/comments`).send({ userId: 'u1', text: 'c1', parentId: null });
    expect(cRes.statusCode).toBe(201);
    const list = await request(app).get(`/playlists/${pid}/comments`);
    expect(list.statusCode).toBe(200);
    expect(list.body.length).toBe(1);
    expect(list.body[0].text).toBe('c1');
  });

  it('should list playlists', async () => {
    const create = await request(app).post('/playlists').send({ userId: 'u1', name: 'P2', songIds: [] });
    const list = await request(app).get('/playlists');
    expect(list.statusCode).toBe(200);
    expect(list.body.find(p => p.id === create.body.id)).toBeTruthy();
  });

  it('should add song to playlist', async () => {
    const create = await request(app).post('/playlists').send({ userId: 'u1', name: 'P3', songIds: [] });
    const add = await request(app).post(`/playlists/${create.body.id}/add`).send({ userId: 'u1', songId: '1' });
    expect(add.statusCode).toBe(201);
    expect(add.body.songIds).toContain('1');
  });
});

describe('board replies', () => {
  it('should add reply post', async () => {
    const p1 = await request(app).post('/board').send({ userId: 'u1', text: 'root' });
    expect(p1.statusCode).toBe(201);
    const reply = await request(app).post('/board').send({ userId: 'u1', text: 'hi', parentId: p1.body.id });
    expect(reply.statusCode).toBe(201);
    const list = await request(app).get('/board');
    expect(list.body.length).toBe(2);
  });
});

describe('user preferences', () => {
  it('should update preferences', async () => {
    const createUser = await request(app).post('/users').send({ name: 'bob' });
    expect(createUser.statusCode).toBe(201);
    const uid = createUser.body.id;
    const post = await request(app).post(`/users/${uid}/preferences`).send({ preferred: ['a'], ignored: ['b'] });
    expect(post.statusCode).toBe(201);
    const get = await request(app).get(`/users/${uid}/preferences`);
    expect(get.body.preferred).toContain('a');
    expect(get.body.ignored).toContain('b');
  });
});

describe('leaderboard', () => {
  it('should order users by points', async () => {
    const u1 = await request(app).post('/users').send({ name: 'alice' });
    const u2 = await request(app).post('/users').send({ name: 'bob' });
    await request(app).post('/songs/1/listen').send({ userId: u1.body.id });
    await request(app).post('/songs/1/listen').send({ userId: u1.body.id });
    await request(app).post('/songs/1/listen').send({ userId: u2.body.id });
    const lb = await request(app).get('/users/leaderboard');
    expect(lb.statusCode).toBe(200);
    expect(lb.body[0].id).toBe(u1.body.id);
  });
});

describe('rating points', () => {
  it('should award points for rating', async () => {
    const u = await request(app).post('/users').send({ name: 'rater' });
    await request(app).post('/songs/1/rate').send({ userId: u.body.id, rating: 'good' });
    const profile = await request(app).get(`/users/${u.body.id}`);
    expect(profile.body.points).toBeGreaterThan(0);
  });
});

describe('wallet collection', () => {
  it('should return owned token ids', async () => {
    global.fetch = jest.fn(() => Promise.resolve({
      ok: true,
      json: () => Promise.resolve({ ownedNfts: [{ tokenId: '1' }] })
    }));
    const res = await request(app).get('/wallet/collection/0xabc');
    expect(res.statusCode).toBe(200);
    expect(res.body).toContain('1');
  });
});

describe('wallet login', () => {
  it('should create or return user for wallet address', async () => {
    global.__verifyMessage = () => '0xabc';
    let res = await request(app).post('/wallet/login').send({ address: '0xabc', signature: 'sig' });
    expect(res.statusCode).toBe(200);
    const id = res.body.id;
    res = await request(app).post('/wallet/login').send({ address: '0xabc', signature: 'sig' });
    expect(res.body.id).toBe(id);
    delete global.__verifyMessage;
  });
});

describe('rating filters and admin actions', () => {
  it('should filter by minRating and delete rating & board post', async () => {
    process.env.ADMIN_KEY = 'adm';
    const user = await request(app).post('/users').send({ name: 'alice' });
    await request(app).post('/songs/1/rate').send({ userId: user.body.id, rating: 'good' });
    let list = await request(app).get('/songs').query({ minRating: 0.5 });
    expect(list.body.length).toBe(1);
    await request(app).delete('/songs/1/rate').send({ userId: user.body.id, key: 'adm' });
    list = await request(app).get('/songs').query({ minRating: 0.5 });
    expect(list.body.length).toBe(0);

    const post = await request(app).post('/board').send({ userId: user.body.id, text: 'hi' });
    expect(post.statusCode).toBe(201);
    const del = await request(app).delete(`/board/${post.body.id}`).query({ key: 'adm' });
    expect(del.statusCode).toBe(204);
    const posts = await request(app).get('/board');
    expect(posts.body.length).toBe(0);
  });
});

describe('comment deletion and admin broken', () => {
  it('should delete comments and resolve broken report', async () => {
    const user = await request(app).post('/users').send({ name: 'alice' });
    const songComment = await request(app)
      .post('/songs/1/comments')
      .send({ userId: user.body.id, text: 'hello' });
    expect(songComment.statusCode).toBe(201);
    const delSongComment = await request(app)
      .delete(`/songs/1/comments/${songComment.body.id}`)
      .send({ userId: user.body.id });
    expect(delSongComment.statusCode).toBe(204);

    const pl = await request(app).post('/playlists').send({ userId: user.body.id, name: 'p1' });
    const plComment = await request(app)
      .post(`/playlists/${pl.body.id}/comments`)
      .send({ userId: user.body.id, text: 'hi' });
    const delPlComment = await request(app)
      .delete(`/playlists/${pl.body.id}/comments/${plComment.body.id}`)
      .send({ userId: user.body.id });
    expect(delPlComment.statusCode).toBe(204);

    await request(app).post('/songs/1/broken').send({ userId: user.body.id });
    const list = await request(app).get('/admin/broken').query({ key: 'adm' });
    expect(list.statusCode).toBe(200);
    const reportId = list.body[0].id;
    const resolve = await request(app)
      .post(`/admin/broken/${reportId}/resolve`)
      .query({ key: 'adm' });
    expect(resolve.statusCode).toBe(200);
  });
});

describe('full listen points', () => {
  it('should award extra points for listening to the whole song', async () => {
    const user = await request(app).post('/users').send({ name: 'listener' });
    app.locals.songsCache[0].attributes = [{ trait_type: 'Length', value: '0:10' }];
    await request(app).post('/songs/1/listen').send({ userId: user.body.id, elapsed: 10 });
    const profile = await request(app).get(`/users/${user.body.id}`);
    expect(profile.body.points).toBe(30); // 20 base + 10 full listen
  });
});
