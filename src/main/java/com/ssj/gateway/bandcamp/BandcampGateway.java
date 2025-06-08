package com.ssj.gateway.bandcamp;

import com.ssj.model.song.Song;

public interface BandcampGateway {

  Song getSong(String songUrl);

}
