import React, { useEffect, useState } from "react";
import "../styles/ReferralForm.css";

const ReferralForm = () => {
    const [pesel, setPesel] = useState("");
    const [firstName, setFirstName] = useState("");
    const [lastName, setLastName] = useState("");
    const [birthDate, setBirthDate] = useState("");
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

        const res = await fetch(`/api/patients/by-pesel/${pesel}`);
        if (res.ok) {
            const data = await res.json();
            setFirstName(data.firstName);
            setLastName(data.lastName);
            setBirthDate(data.birthDate)

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
            patientDto: { firstName, lastName, pesel , birthDate },
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
            <input
                type="text"
                value={birthDate}
                onChange={(e) => setBirthDate(e.target.value)}/>

            <h3>Badania:</h3>
            {tests.map((group) => (
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