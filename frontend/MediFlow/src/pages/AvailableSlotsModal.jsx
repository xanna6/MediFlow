import { useRef } from "react";
import "../styles/AppointmentModal.css";

export default function TimePickerRow({ availableSlots, selectedTime, onSelectTime }) {
    const scrollRef = useRef(null);

    const scrollLeft = () => {
        scrollRef.current.scrollBy({ left: -400, behavior: "smooth" });
    };

    const scrollRight = () => {
        scrollRef.current.scrollBy({ left: 400, behavior: "smooth" });
    };

    return (
        <div className="timepicker-container">
            <button className="scroll-btn left" onClick={scrollLeft}>
                ◀
            </button>

            <div className="timepicker-row" ref={scrollRef}>
                {availableSlots.map((time) => (
                    <button
                        key={time}
                        className={`time-btn ${selectedTime === time ? "selected" : ""}`}
                        onClick={() => onSelectTime(time)}
                    >
                        {time}
                    </button>
                ))}
            </div>

            <button className="scroll-btn right" onClick={scrollRight}>
                ▶
            </button>
        </div>
    );
}
