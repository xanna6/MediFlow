import React, { useEffect, useState } from "react";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import "../styles/ReferralForm.css";

function getBirthDateFromPesel(pesel) {
    if (!/^\d{11}$/.test(pesel)) return null;

    let year = parseInt(pesel.substring(0, 2), 10);
    let month = parseInt(pesel.substring(2, 4), 10);
    let day = parseInt(pesel.substring(4, 6), 10);

    if (month > 0 && month < 13) {
        year += 1900;
    } else if (month > 20 && month < 33) {
        year += 2000;
        month -= 20;
    } else {
        return null;
    }

    const date = new Date(year, month - 1, day);
    return isNaN(date.getTime()) ? null : date;
}

const ReferralForm = () => {
    const [pesel, setPesel] = useState("");
    const [firstName, setFirstName] = useState("");
    const [lastName, setLastName] = useState("");
    const [birthDate, setBirthDate] = useState(null);
    const [tests, setTests] = useState([]);
    const [selectedTests, setSelectedTests] = useState([]);
    const [message, setMessage] = useState("");

    useEffect(() => {
        console.log("wysyłam zapytanie o listę badań")
        fetch("/api/medical-tests/grouped")
            .then((res) => res.json())
            .then(setTests)
            .catch((err) => console.error("Błąd pobierania badań", err));
    }, []);

    const handlePeselBlur = async () => {
        if (pesel.length < 11) return;

        const birthDateFromPesel = getBirthDateFromPesel(pesel);
        if (birthDateFromPesel) {
            setBirthDate(birthDateFromPesel);
        }

        const res = await fetch(`/api/patients/by-pesel/${pesel}`);
        if (res.ok) {
            const data = await res.json();
            setFirstName(data.firstName);
            setLastName(data.lastName);
            setBirthDate(data.birthDate ? new Date(data.birthDate) : null);
            setMessage(`Znaleziono pacjenta: ${data.firstName} ${data.lastName}`);
        } else {
            setFirstName("");
            setLastName("");
            setMessage("Nie znaleziono pacjenta — wprowadź dane.");
        }
    };

    const toggleTest = (id) => {
        setSelectedTests((prev) =>
            prev.includes(id) ? prev.filter((t) => t !== id) : [...prev, id]
        );
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        const payload = {
            patientDto: { firstName, lastName, pesel ,
                birthDate: birthDate ? birthDate.toISOString().split("T")[0] : null },
            doctorId: 1,
            medicalTestIds: selectedTests,
        };

        const res = await fetch("/api/referrals", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(payload),
        });

        if (res.ok) {
            const data = await res.json();
            setMessage(`Skierowanie utworzone (Numer: ${data.referralNumber})`);
            setPesel("");
            setFirstName("");
            setLastName("");
            setBirthDate("")
            setSelectedTests([]);
        } else {
            setMessage("Błąd podczas zapisywania skierowania");
        }
    };

    return (
        <form className="referral-form" onSubmit={handleSubmit}>
            <h2>Nowe skierowanie</h2>

            {
                message && <p className="message">{message}</p>
            }

            <label>PESEL:</label>
            <input
                type="text"
                value={pesel}
                onChange={(e) => setPesel(e.target.value)}
                onBlur={handlePeselBlur}
            />

            <label>Imię:</label>
            <input
                type="text"
                value={firstName}
                onChange={(e) => setFirstName(e.target.value)}
            />

            <label>Nazwisko:</label>
            <input
                type="text"
                value={lastName}
                onChange={(e) => setLastName(e.target.value)}
            />

            <label>Data urodzenia:</label>
            <DatePicker
                selected={birthDate}
                onChange={(date) => setBirthDate(date)}
                dateFormat="dd.MM.yyyy"
                placeholderText="Wybierz datę"
                maxDate={new Date()}
                showMonthDropdown
                showYearDropdown
                dropdownMode="select"
            />

    <h3>Badania:</h3>
    {
        tests.map((group) => (
            <div key={group.category}>
                <h4>{group.category}</h4>
                {group.medicalTests.map((test) => (
                    <div key={test.id} className="checkbox-item">
                        <input
                            type="checkbox"
                            id={`test-${test.id}`}
                                checked={selectedTests.includes(test.id)}
                                onChange={() => toggleTest(test.id)}
                            />
                            <label htmlFor={`test-${test.id}`}>{test.name}</label>
                        </div>
                    ))}
                </div>
            ))}

            <button type="submit" disabled={selectedTests.length === 0}>
                Zapisz skierowanie
            </button>


</form>
);
};

export default ReferralForm;