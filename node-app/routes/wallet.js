import { Router } from 'express';
import db from '../lib/db.js';
import { nanoid } from 'nanoid';
import { verifyMessage } from 'ethers';

function verify(msg, sig) {
  if (global.__verifyMessage) return global.__verifyMessage(msg, sig);
  return verifyMessage(msg, sig);
}

const router = Router();

async function fetchCollection(address) {
  const alchemyKey = process.env.ALCHEMY_API_KEY;
  if (alchemyKey) {
    try {
      const url = `https://eth-mainnet.g.alchemy.com/nft/v3/${alchemyKey}/getNFTs?owner=${address}&contractAddresses[]=${process.env.CONTRACT_ADDRESS}`;
      const res = await fetch(url);
      if (res.ok) {
        const data = await res.json();
        return (data.ownedNfts || []).map(n => n.tokenId || n.token_id);
      }
    } catch (err) {
      console.warn('Alchemy collection lookup failed', err.message);
    }
  }
  const openseaKey = process.env.OPENSEA_API_KEY;
  if (openseaKey) {
    try {
      const url = `https://api.opensea.io/api/v2/chain/ethereum/account/${address}/nfts?asset_contract_address=${process.env.CONTRACT_ADDRESS}`;
      const res = await fetch(url, { headers: { 'X-API-KEY': openseaKey } });
      if (res.ok) {
        const data = await res.json();
        return (data.nfts || []).map(n => n.token_id || n.tokenId);
      }
    } catch (err) {
      console.warn('OpenSea collection lookup failed', err.message);
    }
  }
  return [];
}

const LOGIN_MESSAGE = 'Login to Some Songs';

router.post('/login', async (req, res) => {
  const { address, signature } = req.body;
  if (!address || !signature) return res.status(400).send('Invalid');
  try {
    const recovered = verify(LOGIN_MESSAGE, signature);
    if (recovered.toLowerCase() !== address.toLowerCase()) {
      return res.status(400).send('Signature mismatch');
    }
  } catch (err) {
    return res.status(400).send('Bad signature');
  }
  db.read();
  let user = db.data.users.find(u => u.wallet && u.wallet.toLowerCase() === address.toLowerCase());
  if (!user) {
    user = { id: nanoid(), name: address, wallet: address, points: 0, listeningStreak: 0, ratingStreak: 0, favorites: [], savedSearches: [] };
    db.data.users.push(user);
  }
  db.write();
  res.json(user);
});

router.get('/collection/:address', async (req, res) => {
  const tokens = await fetchCollection(req.params.address);
  res.json(tokens);
});

export default router;
