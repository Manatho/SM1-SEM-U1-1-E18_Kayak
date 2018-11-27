class Point {
	constructor(x, y, rank) {
		this.x = x;
		this.y = y;
		this.rank = rank;
	}

	distance(otherPoint) {
		return Math.sqrt(Math.pow(otherPoint.x - this.x, 2) + Math.pow(otherPoint.y - this.y, 2));
	}
}

class PoorPriorityQueue {
	constructor() {
		this.elements = [];
	}
	get length() {
		return this.elements.length;
	}
	enqueue(element) {
		this.elements.push(element);
	}
	dequeue() {
		let bestIndex = 0;

		this.elements.forEach((element, i) => {
			if (this.elements[i].rank < this.elements[bestIndex].rank) {
				bestIndex = i;
			}
		});

		return this.elements.splice(bestIndex, 1)[0];
	}
}

function closesPixel(waterpixels, centercoords, width, height) {
	let openList = new PoorPriorityQueue();
	let closedList = [];

	let center = new Point(centercoords.x, centercoords.y, 0);
	openList.enqueue(center);

	while (openList.length > 0) {
		let element = openList.dequeue();

		//Pixel found
		if (waterpixels[element.y] && waterpixels[element.y][element.x]) {
			return { x: element.x, y: element.y, d: element.rank };
		}

		neightbours(element).forEach(neightbour => {
			if (neightbour.x >= 0 && neightbour.x < width && neightbour.y >= 0 && neightbour.y < height) {
				if (closedList[neightbour.y] == null) {
					closedList[neightbour.y] = [];
				}

				if (closedList[neightbour.y][neightbour.x] == null) {
					neightbour.rank = neightbour.distance(center);
					openList.enqueue(neightbour);
					closedList[neightbour.y][neightbour.x] = neightbour;
				}
			}
		});
	}
}

let dirs = [
	new Point(-1, -1),
	new Point(0, -1),
	new Point(1, -1),
	new Point(1, 0),
	new Point(-1, 0),
	new Point(-1, 1),
	new Point(0, 1),
	new Point(1, 1)
];

function neightbours(element) {
	result = [];
	dirs.forEach(dir => {
		result.push(new Point(element.x + dir.x, element.y + dir.y));
	});
	return result;
}

module.exports = {
	closesPixel
};
