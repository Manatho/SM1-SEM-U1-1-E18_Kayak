module.exports = {
	movingAverage: movingAverage,
	movingMedian: movingMedian
};

function movingAverage(boxSpread, data) {
	filteredData = [];
	debug = "";

	for (let i = 0; i < data.length; i++) {
		let summedLon = 0;
		let summedLat = 0;
		let boxSize = boxSpread * 2 + 1;

		for (let proposedIndex = i - boxSpread; proposedIndex < i + boxSpread + 1; proposedIndex++) {
			let actualIndex = Math.min(data.length - 1, Math.max(0, proposedIndex));
			summedLat += parseFloat(data[actualIndex].latitude);
			summedLon += parseFloat(data[actualIndex].longitude);
		}

		filteredData[i] = { latitude: summedLat / boxSize, longitude: summedLon / boxSize, time: data[i].time };
		debug += i + ": latitude/longitude: " + filteredData[i].latitude.toFixed(8) + ", " + filteredData[i].longitude.toFixed(8) + "\n";
	}
	return {
		data: filteredData,
		debuggingOutput: debug
	};
}

function movingMedian(boxSpread, data) {
	filteredData = [];
	debug = "";

	for (let i = 0; i < data.length; i++) {
		let longs = [];
		let lats = [];

		for (let proposedIndex = i - boxSpread; proposedIndex < i + boxSpread + 1; proposedIndex++) {
			let actualIndex = Math.min(data.length - 1, Math.max(0, proposedIndex));

			longs.push(data[actualIndex].longitude);
			lats.push(data[actualIndex].latitude);
		}

		longs.sort();
		lats.sort();

		console.log(longs, lats);

		filteredData[i] = { latitude: lats[boxSpread], longitude: longs[boxSpread], time: data[i].time };
		debug += i + ": latitude/longitude: " + filteredData[i].latitude.toFixed(8) + ", " + filteredData[i].longitude.toFixed(8) + "\n";
	}

	return {
		data: filteredData,
		debuggingOutput: debug
	};
}
