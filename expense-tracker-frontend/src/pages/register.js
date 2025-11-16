// src/pages/register.js
import { useState, useEffect } from "react";
import { useRouter } from "next/router";

export default function Register() {
  const [formData, setFormData] = useState({
    name: "",
    email: "",
    password: "",
    tenantId: "",
  });
  const [tenants, setTenants] = useState([]);
  const [message, setMessage] = useState("");
  const router = useRouter();

  // Fetch tenants on load
  useEffect(() => {
    const fetchTenants = async () => {
      try {
        const res = await fetch("http://localhost:8080/api/tenants"); // your endpoint to get all tenants
        if (!res.ok) throw new Error("Failed to fetch tenants");
        const data = await res.json();
        setTenants(data);
      } catch (err) {
        console.error(err);
        setMessage("Could not load companies");
      }
    };
    fetchTenants();
  }, []);

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
  e.preventDefault();
  setMessage(""); // clear old messages

  try {
    const res = await fetch("http://localhost:8080/api/auth/register", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(formData),
    });

    if (res.ok) {
      // Optionally parse response if needed
      // const data = await res.json();

      // Redirect after successful registration
      router.push("/");
    } else {
      // Parse error body safely
      let data;
      try {
        data = await res.json();
      } catch {
        data = { message: "Registration failed" };
      }
      setMessage(data.message);
    }
  } catch (err) {
    console.error("Registration error:", err);
    setMessage("Network error");
  }
};

  return (
    <div className="container">
      <h1>Register</h1>
      <form onSubmit={handleSubmit}>
        <input
          type="text"
          name="name"
          placeholder="Full Name"
          onChange={handleChange}
          required
        />
        <input
          type="email"
          name="email"
          placeholder="Email"
          onChange={handleChange}
          required
        />
        <input
          type="password"
          name="password"
          placeholder="Password"
          onChange={handleChange}
          required
        />
        <select
          name="tenantId"
          value={formData.tenantId}
          onChange={handleChange}
          required
        >
          <option value="">Select Company</option>
          {tenants.map((tenant) => (
            <option key={tenant.id} value={tenant.id}>
              {tenant.name}
            </option>
          ))}
        </select>
        <button type="submit">Register</button>
      </form>
      {message && <p>{message}</p>}
    </div>
  );
}