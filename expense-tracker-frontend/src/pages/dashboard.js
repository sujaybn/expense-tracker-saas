import { useEffect, useState } from "react";
import { useRouter } from "next/router";
import styles from "../styles/dashboard.module.css";

export default function Dashboard() {
  const router = useRouter();
  const [user, setUser] = useState(null);
  const [expenses, setExpenses] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  // Load logged-in user from localStorage
  useEffect(() => {
  const storedUser = localStorage.getItem("user");
  if (!storedUser) {
    router.push("/"); 
    return;
  }

  const parsedUser = JSON.parse(storedUser);
  setUser(parsedUser);

  const fetchExpenses = async () => {
    setLoading(true);
    setError("");
    try {
      const res = await fetch("http://localhost:8080/api/expenses", {
        method: "GET",
        headers: { "Content-Type": "application/json" },
        credentials: "include",
      });
      if (!res.ok) throw new Error("Failed to fetch expenses");
      const data = await res.json();
      setExpenses(data);
    } catch (err) {
      console.error(err);
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  fetchExpenses();
}, [router]);

  // Fetch expenses if user is a manager
  useEffect(() => {
  const storedUser = localStorage.getItem("user");
  if (!storedUser) {
    router.push("/"); // redirect if not logged in
    return;
  }

  const parsedUser = JSON.parse(storedUser);
  setUser(parsedUser);

  // fetch expenses only after user is loaded
  const fetchExpenses = async () => {
    setLoading(true);
    setError("");
    try {
      const res = await fetch("http://localhost:8080/api/expenses", {
        method: "GET",
        headers: { "Content-Type": "application/json" },
        credentials: "include",
      });
      if (!res.ok) throw new Error("Failed to fetch expenses");
      const data = await res.json();
      setExpenses(data);
    } catch (err) {
      console.error("Failed to fetch expenses:", err);
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  fetchExpenses();
}, [router]);
  const handleLogout = () => {
    localStorage.removeItem("user");
    router.push("/");
  };

  if (!user) return <div className={styles.container}>Loading...</div>;

  return (
    <div className={styles.container}>
      <h1>
        Welcome, {user.name}! <br />
        You are logged in as <strong>{user.role}</strong>
      </h1>

      <button onClick={handleLogout} className={styles.logoutButton}>
        Logout
      </button>

      {user.role === "MANAGER" && (
        <div className={styles.expensesSection}>
          <h2>Expenses for your organization</h2>
          {loading ? (
            <p>Loading expenses...</p>
          ) : error ? (
            <p className={styles.error}>{error}</p>
          ) : expenses.length === 0 ? (
            <p>No expenses found.</p>
          ) : (
            <table className={styles.expensesTable}>
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Title</th>
                  <th>Amount</th>
                  <th>Date</th>
                  <th>Status</th>
                </tr>
              </thead>
              <tbody>
                {expenses.map((exp) => (
                  <tr key={exp.id}>
                    <td>{exp.id}</td>
                    <td>{exp.title}</td>
                    <td>{exp.amount}</td>
                    <td>{new Date(exp.date).toLocaleDateString()}</td>
                    <td>{exp.status}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>
      )}
    </div>
  );
}