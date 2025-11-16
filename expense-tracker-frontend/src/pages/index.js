import { useState } from "react";
import { useRouter } from "next/router";
import '../styles/login.module.css';

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
    <div className="container">
      <h1>Login</h1>
      <form onSubmit={handleSubmit}>
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
        <button type="submit">Login</button>
      </form>
      <p>
        Don't have an account? <a href="/register">Register</a>
      </p>
      <div className="message">{message}</div>

      {/* <style jsx>{`
        .container {
          max-width: 400px;
          margin: 80px auto;
          padding: 20px;
          border: 1px solid #ccc;
          border-radius: 8px;
          text-align: center;
        }
        input {
          width: 100%;
          padding: 10px;
          margin: 10px 0;
        }
        button {
          width: 100%;
          padding: 10px;
          background: #0070f3;
          color: white;
          border: none;
          cursor: pointer;
        }
        button:hover {
          background: #005bb5;
        }
        .message {
          color: red;
          margin-top: 10px;
        }
        a {
          color: #0070f3;
        }
      `}</style> */}
    </div>
  );
}