const getPixel = require("get-pixels");

function getImageAtCoord(coord) {
  let url = "https://maps.googleapis.com/maps/api/staticmap?";

  let params = {
    key: "AIzaSyAy4urcUkronsRlssePjURjnG7RcPzM1ys",
    center: `${coord.latitude},${coord.longitude}`,
    zoom: 18,
    size: "48x96",
    style:
      "feature:all|visibility:off&style=feature:water|visibility:on&style=feature:water|color:0x0000FF"
  };

  for (let key in params) {
    url += `&${key}=${params[key]}`;
  }

  return new Promise((resolve, reject) => {
    getPixel(url, (err, pixels) => {
      if (err) return reject(err);
      resolve(pixels)
    });
  });
}

module.exports = {
  async process(points) {
    const pixels = await getImageAtCoord({ latitude: 55.3496028, longitude: 10.361572 });

    let { shape } = pixels;
    let width = shape[0];
    let height = shape[1];

    let pixelCount = 0;
    let sum = 0;
    for (let x = 0; x < width; x++) {
      for (
        let y = Math.round(height * 0.25);
        y < Math.round(height * 0.75);
        y++
      ) {
        pixelCount++;
        sum += pixels.get(x, y, 3);
      }
    }
    let waterPercentage = sum / pixelCount;
    console.log(
      `Water percentage = ${Math.round(waterPercentage * 100) / 100}%`
    );
  }
};
