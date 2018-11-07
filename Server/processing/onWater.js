const getPixel = require("get-pixels");
const closestWaterPixel = require("./closestWaterPixel").closesPixel;

function getImageAtCoord(coord) {
	let url = "https://maps.googleapis.com/maps/api/staticmap?";

	let params = {
		key: "AIzaSyAy4urcUkronsRlssePjURjnG7RcPzM1ys",
		center: `${coord.latitude},${coord.longitude}`,
		zoom: 18,
		size: "48x96",
		style: "feature:all|visibility:off&style=feature:water|visibility:on&style=feature:water|color:0x0000FF"
	};

	for (let key in params) {
		url += `&${key}=${params[key]}`;
	}

	return new Promise((resolve, reject) => {
		getPixel(url, (err, pixels) => {
			if (err) return reject(err);
			resolve(pixels);
		});
	});
}

module.exports = {
	async process(points) {
		let text = "";

		//const pixels = await getImageAtCoord({ latitude: 55.3496028, longitude: 10.361572 });

		for (let i = 0; i < points.length; i++) {
			const point = points[i];

			let pixels = await getImageAtCoord({ latitude: point.latitude, longitude: point.longitude });
			let { shape } = pixels;
			let width = shape[0];
			let height = shape[1];

			let yStartIndex = Math.round(height * 0.25),
				yEndIndex = Math.round(height * 0.75);
			let centercoords = { x: width / 2, y: (yEndIndex - yStartIndex) / 2 };
			let watercoords = [];
			let pixelCount = (yEndIndex - yStartIndex) * width;

			let sum = 0;
			for (let x = 0; x < width; x++) {
				for (let y = yStartIndex; y < yEndIndex; y++) {
					let value = pixels.get(x, y, 3);
					let yoff = y - yStartIndex;

					if (value != 0) {
						sum++;
						if (watercoords[yoff] == null) {
							watercoords[yoff] = [];
						}
						watercoords[yoff][x] = 1;
					}
				}
			}
			let waterPercentage = sum / pixelCount;

			if (sum != 0) {
				let result = closestWaterPixel(watercoords, centercoords, width, yEndIndex - yStartIndex);
				text += drawPng(width, yEndIndex - yStartIndex, watercoords, result);
				text += `center:  ${JSON.stringify(centercoords)} Nearst water: ${JSON.stringify(result)}\n`;
			} else {
				text += "Couldn't find any water\n";
			}

			text += `Water percentage = ${(waterPercentage * 100).toFixed(2)}%\n`;
		}
		return text;
	}
};

function drawPng(width, height, waterpixels, result) {
	let text = "";
	for (let y = 0; y < height; y++) {
		for (let x = 0; x < width; x++) {
			if (x == (width / 2) >> 0 && y == (height / 2) >> 0) {
				text += "_C_";
			} else if (x == result.x && y == result.y) {
				text += "___";
			} else if (waterpixels[y] != null) {
				text += waterpixels[y][x] ? "▒▒▒" : "███";
			} else {
				text += "███";
			}
		}
		text += "\n";
	}
	return text;
}
