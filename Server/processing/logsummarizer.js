module.exports = {
	process(data) {
		let totalSpeed = 0;
		let totalDistance = 0;
		let minSpeed = Number.MAX_VALUE;
		let maxSpeed = 0;
		let debugging = "";

		for (let i = 0; i < data.length; i++) {
			const element = data[i];
			const nextElement = data[i + 1];

			if (nextElement == undefined) {
				element.speed = 0;
				debugging +=
					i +
					" latitude/longitude: " +
					element.latitude.toFixed(8) +
					", " +
					element.longitude.toFixed(8) +
					" Speed: " +
					element.speed +
					" Distance to Next: " +
					0 +
					"\n";
				break;
			}

			let distanceToNextMeters = distance(element, nextElement);
			let deltaSeconds = (nextElement.time - element.time) / 1000;
			element.speed = (distanceToNextMeters / 1000 / deltaSeconds) * 60 * 60;
			element.distanceToNext = distanceToNextMeters;

			totalDistance += distanceToNextMeters;
			totalSpeed += element.speed;

			if (element.speed < minSpeed) {
				minSpeed = element.speed;
			}

			if (element.speed > maxSpeed) {
				maxSpeed = element.speed;
			}

			debugging +=
				i +
				" latitude/longitude: " +
				element.latitude.toFixed(8) +
				", " +
				element.longitude.toFixed(8) +
				" Speed: " +
				element.speed +
				" Distance to Next: " +
				element.distanceToNext +
				"\n";
		}

		return {
			averageSpeed: totalSpeed / data.length,
			minSpeed: minSpeed,
			maxSpeed: maxSpeed,
			totalDistance: totalDistance / 1000,
			duration: data[data.length - 1].time - data[0].time,
			debuggingOutput: `AVERAGE SPEED: ${totalSpeed / data.length} MIN SPEED: ${minSpeed} MAX SPEED: ${maxSpeed} DISTANCE: ${(
				totalDistance / 1000
			).toFixed(3)} DURATION: ${((data[data.length - 1].time - data[0].time) / 1000).toFixed(3)} |
            ${debugging}
            `
		};
	}
};

function distance(point1, point2) {
	var R = 6371000; // Radius of the earth in m
	var dLat = degreesToRad(point2.latitude - point1.latitude); // deg2rad below
	var dLon = degreesToRad(point2.longitude - point1.longitude);
	var a =
		Math.sin(dLat / 2) * Math.sin(dLat / 2) +
		Math.cos(degreesToRad(point1.latitude)) * Math.cos(degreesToRad(point2.latitude)) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
	var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
	var d = R * c; // Distance in m
	return d;
}

function degreesToRad(deg) {
	return deg * (Math.PI / 180);
}
