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
		const pixels = await getImageAtCoord({ latitude: 55.3497, longitude: 10.361572 });
		//const pixels = await getImageAtCoord({ latitude: 55.3496028, longitude: 10.361572 });

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
			drawPng(width, yEndIndex - yStartIndex, watercoords, result);
			console.log("center: ", centercoords, "Nearst water: ", result);
		} else {
			console.log("Couldn't find any water");
		}

		console.log(`Water percentage = ${(waterPercentage * 100).toFixed(2)}%`);
	}
};

function drawPng(width, height, waterpixels, result) {
	for (let y = 0; y < height; y++) {
		let text = "";
		for (let x = 0; x < width; x++) {
			if (x == (width / 2) >> 0 && y == (height / 2) >> 0) {
				text += " C ";
			} else if (x == result.x && y == result.y) {
				text += "   ";
			} else if (waterpixels[y] != null) {
				text += waterpixels[y][x] ? "▒▒▒" : "███";
			} else {
				text += "███";
			}
		}
		console.log(text);
	}
}
