module.exports = {
	calculatePoints: calculatePoints
};

function calculatePoints(log, data) {
	let totalPoints = 0;
	let lastPoint = data[0];
	data.forEach(point => {
		if (point.speed < 25 && point.waterDistance < 5) {
			point.valid = true;
			totalPoints += lastPoint.distanceToNext;
		} else {
			point.valid = false;
		}
		lastPoint = point;
	});

	log.points = totalPoints << 0;
}
