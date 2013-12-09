/*
 * (C) Copyright 2013 Kurento (http://kurento.org/)
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-2.1.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 */
package com.kurento.demo.campusparty;

import com.kurento.kmf.content.HttpPlayerHandler;
import com.kurento.kmf.content.HttpPlayerService;
import com.kurento.kmf.content.HttpPlayerSession;
import com.kurento.kmf.media.JackVaderFilter;
import com.kurento.kmf.media.MediaPipeline;
import com.kurento.kmf.media.MediaPipelineFactory;
import com.kurento.kmf.media.PlayerEndPoint;

/**
 * HTTP Player Handler; tunnel strategy; JSON control protocol; it creates a
 * player end point in the media server with a WEBM video, and a Jack Vader
 * Filter is connected to this player. This filter detects human faces an put
 * them a pirate hut.
 * 
 * @author Luis López (llopez@gsyc.es)
 * @author Boni García (bgarcia@gsyc.es)
 * @version 1.0.0
 */
@HttpPlayerService(name = "PlayerJsonFilter", path = "/playerJsonFilter", redirect = true, useControlProtocol = true)
public class PlayerJsonJackVader extends HttpPlayerHandler {

	@Override
	public void onContentRequest(HttpPlayerSession session) throws Exception {
		MediaPipelineFactory mpf = session.getMediaPipelineFactory();
		MediaPipeline mp = mpf.create();
		session.releaseOnTerminate(mp);
		PlayerEndPoint playerEndPoint = mp.newPlayerEndPoint(
				"https://ci.kurento.com/video/fiwarecut.webm").build();
		JackVaderFilter filter = mp.newJackVaderFilter().build();
		playerEndPoint.connect(filter);
		session.setAttribute("player", playerEndPoint);
		session.start(filter);
	}

	@Override
	public void onContentStarted(HttpPlayerSession session) {
		PlayerEndPoint playerEndPoint = (PlayerEndPoint) session
				.getAttribute("player");
		playerEndPoint.play();
	}

}