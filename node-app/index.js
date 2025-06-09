import 'dotenv/config';
import express from 'express';
import { algoliasearch } from 'algoliasearch';
import cron from 'node-cron';
import path from 'path';
import { fileURLToPath } from 'url';

import songsRouter from './routes/songs.js';
import usersRouter from './routes/users.js';
import playlistsRouter from './routes/playlists.js';
import boardRouter from './routes/board.js';
import walletRouter from './routes/wallet.js';
import adminRouter from './routes/admin.js';

const app = express();
app.use(express.json());
const __dirname = path.dirname(fileURLToPath(import.meta.url));
app.use(express.static(path.join(__dirname, 'public')));

const client = algoliasearch(process.env.ALGOLIA_APP_ID || '', process.env.ALGOLIA_SEARCH_KEY || '');
const index = client.initIndex(process.env.ALGOLIA_INDEX_NAME || 'songs');

async function fetchSaleInfo(tokenId) {
  const alchemyKey = process.env.ALCHEMY_API_KEY;
  if (alchemyKey) {
    try {
      const url = `https://eth-mainnet.g.alchemy.com/nft/v3/${alchemyKey}/getNFTSales?fromBlock=0&toBlock=latest&order=desc&contractAddress=${process.env.CONTRACT_ADDRESS}&tokenId=${tokenId}&limit=1`;
      const res = await fetch(url);
      if (res.ok) {
        const data = await res.json();
        if (data.nftSales && data.nftSales.length) {
          return { forSale: false, lastSale: data.nftSales[0].blockTimestamp };
        }
      }
    } catch (err) {
      console.warn('Alchemy lookup failed', err.message);
    }
  }
  const openseaKey = process.env.OPENSEA_API_KEY;
  if (openseaKey) {
    try {
      const url = `https://api.opensea.io/api/v2/orders/ethereum/seaport/listings?asset_contract_address=${process.env.CONTRACT_ADDRESS}&token_ids=${tokenId}&order_by=created_date&limit=1`;
      const res = await fetch(url, { headers: { 'X-API-KEY': openseaKey } });
      if (res.ok) {
        const data = await res.json();
        return { forSale: data.orders && data.orders.length > 0, lastSale: null };
      }
    } catch (err) {
      console.warn('OpenSea lookup failed', err.message);
    }
  }
  return { forSale: false, lastSale: null };
}

async function fetchSongs() {
  try {
    const { hits } = await index.search('', { hitsPerPage: 1000 });
    for (const song of hits) {
      const info = await fetchSaleInfo(song.token_id);
      song.forSale = info.forSale;
      song.lastSale = info.lastSale;
    }
    app.locals.songsCache = hits;
  } catch (err) {
    console.error('Failed to fetch songs from Algolia:', err.message);
  }
}

cron.schedule('0 0 * * *', fetchSongs);

app.use('/songs', songsRouter);
app.use('/users', usersRouter);
app.use('/playlists', playlistsRouter);
app.use('/board', boardRouter);
app.use('/wallet', walletRouter);
app.use('/admin', adminRouter);

app.get('/', (req, res) => {
  res.send('Some Songs API');
});

app.listen(3000, () => {
  fetchSongs();
  console.log('Server running on port 3000');
});
