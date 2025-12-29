import { MapContainer, TileLayer, Marker, Popup, useMap } from "react-leaflet";
import L from "leaflet";
import "leaflet/dist/leaflet.css";
import React from "react";

const defaultIcon = new L.Icon({
    iconUrl: "https://unpkg.com/leaflet@1.7.1/dist/images/marker-icon.png",
    shadowUrl: "https://unpkg.com/leaflet@1.7.1/dist/images/marker-shadow.png",
    iconSize: [25, 41],
    iconAnchor: [12, 41],
});

const selectedIcon = new L.Icon({
    iconUrl:
        "https://raw.githubusercontent.com/pointhi/leaflet-color-markers/master/img/marker-icon-red.png",
    shadowUrl:
        "https://unpkg.com/leaflet@1.7.1/dist/images/marker-shadow.png",
    iconSize: [25, 41],
    iconAnchor: [12, 41],
});

function FlyToPoint({ point }) {
    const map = useMap();

    React.useEffect(() => {
        if (point?.latitude && point?.longitude) {
            map.flyTo([point.latitude, point.longitude], 15, {
                duration: 1,
            });
        }
    }, [point, map]);

    return null;
}

export default function CollectionPointsMap({
                                                points,
                                                selectedPoint,
                                                onSelect,
                                            }) {
    return (
        <div className="points-map">
            <MapContainer
                center={[52.2297, 21.0122]}
                zoom={12}
                style={{ height: "100%", width: "100%" }}
            >
                <TileLayer
                    url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                />

                {selectedPoint && <FlyToPoint point={selectedPoint} />}

                {points.map((p) => (
                    <Marker
                        key={p.id}
                        position={[p.latitude, p.longitude]}
                        icon={
                            selectedPoint?.id === p.id
                                ? selectedIcon
                                : defaultIcon
                        }
                        eventHandlers={{
                            click: () => onSelect(p),
                        }}
                    >
                        <Popup>
                            <strong>{p.name}</strong>
                            <br />
                            {p.street}, {p.city}
                        </Popup>
                    </Marker>
                ))}
            </MapContainer>
        </div>
    );
}
