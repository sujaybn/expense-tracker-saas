import { useState, useEffect } from "react";
import { useRouter } from "next/router";
import styles from "../styles/register.module.css";

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
        const res = await fetch("http://localhost:8080/api/tenants");
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
    setMessage("");

    try {
      const res = await fetch("http://localhost:8080/api/auth/register", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(formData),
      });

      if (res.ok) {
        router.push("/"); // Redirect after successful registration
      } else {
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
    <div className={styles.container}>
      <h1 className={styles.title}>Create Account</h1>
      <form onSubmit={handleSubmit}>
        <input
          type="text"
          name="name"
          placeholder="Full Name"
          onChange={handleChange}
          required
          className={styles.input}
        />
        <input
          type="email"
          name="email"
          placeholder="Email"
          onChange={handleChange}
          required
          className={styles.input}
        />
        <input
          type="password"
          name="password"
          placeholder="Password"
          onChange={handleChange}
          required
          className={styles.input}
        />
        <select
          name="tenantId"
          value={formData.tenantId}
          onChange={handleChange}
          required
          className={styles.input}
        >
          <option value="">Select Company</option>
          {tenants.map((tenant) => (
            <option key={tenant.id} value={tenant.id}>
              {tenant.name}
            </option>
          ))}
        </select>
        <button type="submit" className={styles.button}>
          Register
        </button>

        <p className={styles.link}>
        Alredy have an account? <a href="/">Login</a>
      </p>
      </form>
      {message && <p className={styles.message}>{message}</p>}
    </div>
  );
}