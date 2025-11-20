import { useState } from "react";
import { useRouter } from "next/router";
import styles from '../styles/login.module.css';

export default function Login() {
  const [formData, setFormData] = useState({ email: "", password: "" });
  const [message, setMessage] = useState("");
  const router = useRouter();

  const handleChange = e => setFormData({ ...formData, [e.target.name]: e.target.value });

  const handleSubmit = async e => {
    e.preventDefault();
    try {
      const res = await fetch("http://localhost:8080/api/auth/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(formData),
      });

      const text = await res.text();
      if (!text) {
        setMessage("Login failed: no data returned from server");
        return;
      }

      const data = JSON.parse(text);

      if (res.ok) {
        localStorage.setItem("user", JSON.stringify(data));
        router.push("/dashboard");
      } else {
        setMessage(data.message || "Login failed");
      }
    } catch (err) {
      console.error(err);
      setMessage("Network error or server not reachable");
    }
  };

  return (
    <div className={styles.container}>
      <h1 className={styles.title}>Welcome Back</h1>      
      <form onSubmit={handleSubmit}>
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
        <button type="submit" className={styles.button}>Login</button>
      </form>
      <p className={styles.link}>
        Don't have an account? <a href="/register">Register</a>
      </p>
      {message && <div className={styles.message}>{message}</div>}
    </div>
  );
}