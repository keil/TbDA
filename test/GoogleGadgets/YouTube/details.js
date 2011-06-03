/*
Copyright (C) 2008 Google Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

function detailsOnOpen() {
  var curItem = detailsViewData.getValue("curItem");
  var closeDetailsView = detailsViewData.getValue("closeDetailsView");

  close.onclick = closeDetailsView;
  title.onclick = closeDetailsView;

  if (curItem) {
    title.href = curItem.url;
    title.innerText = curItem.title;

    description.innerText = curItem.description;
    more_link.href = curItem.url;
    noEmbedLink.href = curItem.url;

    total.innerText = strings.RATINGS.replace(
        '[![TOTAL_RATERS]!]', curItem.numRaters);
    view_count.innerText = strings.VIEWERS.replace(
        '[![VIEWS]!]', curItem.viewCount);

    for (var i = 0; i < 5; ++i) {
      if (curItem.rating < i + 0.45) {
        ratings.children('rating' + i).src = 'images/details/star_empty.png';
      } else if (curItem.rating < i + 0.7) {
        ratings.children('rating' + i).src = 'images/details/star_half.png';
      } else {
        ratings.children('rating' + i).src = 'images/details/star_full.png';
      }
    }

    var playerError = true;
    if (ytplayer.object) {
      if (Util.isWindows()) {
        // Taken from swfobject 2.0
        try {
          var v = ytplayer.object.GetVariable("$version");  // Will crash flash
                                                            // v 6.0.21/23/29
          debug.info('flash version: ' + v);
          if (v) {
            v = v.split(" ")[1].split(",");
            var flashMajorVersion = parseInt(v[0], 10);
            if (flashMajorVersion >= 8) {
              playerError = false;
            }
          }
        } catch(e) {}
      } else if (Util.isLinux()) {
        try {
          if (ytplayer.object.GetVariable) {
            playerError = false;
          }
        } catch(e) {}
      }
    }

    if (playerError) {
      // Show an error message if the flash player is unavailable
      player_container.visible = false;
      error.visible = true;
    } else if (!curItem.embeddedurl) {
      player_container.visible = false;
      noEmbed.visible = true;
    } else {
      ytplayer.object.movie = curItem.embeddedurl + '&autoplay=1';
    }
  }
}
