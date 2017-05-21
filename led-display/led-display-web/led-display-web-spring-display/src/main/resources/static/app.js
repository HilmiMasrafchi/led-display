var stompClient = null;

var ledWidth = 25;
var ledHeight = 25;

var frameIndex;
var frameCount;
var skipFrameUntil = 4;
var animationCounter = 0;
var animationId;

var ctx; 

var requestAnimationFrame = window.requestAnimationFrame
		|| window.mozRequestAnimationFrame
		|| window.webkitRequestAnimationFrame || window.msRequestAnimationFrame;

function drawCurrentFrame() {
	animationCounter++;
	if (animationCounter > skipFrameUntil) {
		ctx.clearRect(0, 0, canvasWidth, canvasHeight);

		var ledRows = compiledFrames[frameIndex];
		for ( var frameRowIndex in ledRows) {
			var frameRowLeds = ledRows[frameRowIndex];
			for (var ledIndex = 0; ledIndex < frameRowLeds.length; ledIndex++) {
				var led = frameRowLeds[ledIndex];

				var ledColor = led.rgbColor;
				var red = ledColor.r;
				var green = ledColor.g;
				var blue = ledColor.b;
				var color = 'rgb(' + red + ', ' + green + ', ' + blue + ')';
				ctx.fillStyle = color;

				ctx.fillRect((ledIndex * ledWidth) + 5, frameRowIndex
						* ledHeight, ledWidth, ledHeight);
			}
		}

		frameIndex++;
		if (frameIndex == frameCount) {
			frameIndex = 0;
		}

		animationCounter = 0;
	}

	animationId = requestAnimationFrame(drawCurrentFrame);
}

function getMatrix() {
	var socket = new SockJS('/gs-guide-websocket');
	var stompClientGetMatrix = Stomp.over(socket);
	stompClientGetMatrix.connect({}, function(frame) {
		console.log('Connected: ' + frame);
		var matrixId = $.QueryString.matrixId
		stompClientGetMatrix.subscribe('/app/matrix/' + matrixId, function(
				event) {
			cancelAnimationFrame(animationId);

			var matrix = JSON.parse(event.body);

			compiledFrames = matrix.compiledFrames;

			var rowCount = matrix.rowCount;
			var columnCount = matrix.columnCount;

			canvasWidth = columnCount * ledWidth;
			canvasHeight = rowCount * ledHeight;

			canvas.width = canvasWidth;
			canvas.height = canvasHeight;

			frameIndex = 0;
			frameCount = compiledFrames.length;

			animationCounter = 0;

			drawCurrentFrame();

			if (stompClientGetMatrix != null) {
				stompClientGetMatrix.disconnect();
			}
		});
	});
	
}

function subscribeToMatrixUpdatesTopic() {
	var socket = new SockJS('/gs-guide-websocket');
	var stompClient = Stomp.over(socket);
	stompClient.connect({}, function(frame) {
		console.log('Connected: ' + frame);
		var matrixId = $.QueryString.matrixId
		stompClient.subscribe('/topic/matrixupdates/' + matrixId, function(
				event) {
			cancelAnimationFrame(animationId);

			var matrix = JSON.parse(event.body);

			compiledFrames = matrix.compiledFrames;

			var rowCount = matrix.rowCount;
			var columnCount = matrix.columnCount;

			canvasWidth = columnCount * ledWidth;
			canvasHeight = rowCount * ledHeight;

			canvas.width = canvasWidth;
			canvas.height = canvasHeight;

			frameIndex = 0;
			frameCount = compiledFrames.length;

			animationCounter = 0;

			drawCurrentFrame();
		});
	});
}

(function($) {
	$.QueryString = (function(a) {
		if (a == "")
			return {};
		var b = {};
		for (var i = 0; i < a.length; ++i) {
			var p = a[i].split('=', 2);
			if (p.length != 2)
				continue;
			b[p[0]] = decodeURIComponent(p[1].replace(/\+/g, " "));
		}
		return b;
	})(window.location.search.substr(1).split('&'))
})(jQuery);

$(document).ready(function() {
	var canvas = document.getElementById('canvas');
	ctx = canvas.getContext('2d');

	getMatrix();
	subscribeToMatrixUpdatesTopic();
});