import React, { Component } from "react";

export class Map extends Component {
  componentDidMount() {
    const { coordinates } = this.props;
    const [lat, lon] = coordinates.split(' ');
    let map;

    window.DG.then(function() {
      map = window.DG.map("map$Id$", {
        center: [lat, lon],
        zoom: 14
      });

      window.DG.marker([lat, lon]).addTo(map);
    });
  }

  render() {
    return <div id="map$Id$" className="map" />;
  }
}
