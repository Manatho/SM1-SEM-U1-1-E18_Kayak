const axios = require("axios");
const getPixel = require("get-pixels");

module.exports = {
  async process(points) {
    return await axios.default
      .get(
        "https://maps.googleapis.com/maps/api/staticmap?&center=55.3496028,10.361572&zoom=18&size=48x96&style=feature:all%7Cvisibility:off&style=feature:water%7Cvisibility:on&style=feature:water%7Ccolor:0x0000FF"
        //   {
        //     params: {
        //       center: "55.3496028,10.361572",
        //       zoom: 18,
        //       size: "48z96",
        //       style: "feature:all|visibility:off",
        //       style: "feature:water|visibility:on",
        //       style: "feature:water|color:0x0000FF"
        //     }
        //   }
      )
  }
};
