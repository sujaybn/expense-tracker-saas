import { useEffect, useState } from "react";
import { useRouter } from "next/router";
import styles from "../styles/dashboard.module.css";

export default function CreateExpense() {
  const router = useRouter();
  const [user, setUser] = useState(null);
  const [amount, setAmount] = useState(null);
  const [description, setDescription] = useState(null);
  const [date, setDate] = useState(null);
  const [category, setCategory] = useState(null);
  
  const handleSubmit = async (event) => {
    event.preventDefault();
    const formData = {
      amount:amount,
      description:description,
      date:date,
      categoryId:category,
      tenantId:user.tenant.id
    }
    console.log(formData);
    try {
          const params = { userId: JSON.parse(localStorage.getItem("user")).id };
          const url = new URL('http://localhost:8080/api/expenses');
          Object.keys(params).forEach(key => url.searchParams.append(key, params[key]));
          console.log(url);
          
          const res = await fetch(url, {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(formData)
        });
        if (!res.ok) 
          throw new Error("Failed to create expenses");
      } catch (err) {
        console.error("Failed to create expenses:", err);
        setError(err.message);
      } 
  };
  useEffect(() => {
    console.log("here");
    const storedUser = localStorage.getItem("user");
    if (!storedUser) {
      router.push("/");
      return;
    }

    const parsedUser = JSON.parse(storedUser);
    setUser(parsedUser);

  }, []);
  const handleLogout = () => {
    localStorage.removeItem("user");
    router.push("/");
  };

  const handleDashboard = () => {
    router.push("/dashboard");
  };

  if (!user) return <div className={styles.container}>Loading...</div>;

  return (
    <div className={styles.container}>
      <h1>
        Welcome, {user.name}! <br />
        You are logged in as <strong>{user.role}</strong>
      </h1>

      <button onClick={handleDashboard} className={styles.logoutButton}>
        Dashboard
      </button>
            <br></br>
      <button onClick={handleLogout} className={styles.logoutButton}>
        Logout
      </button>
      <div className={styles.expensesSection}>
        <h2>Add expense</h2>
        <form onSubmit={handleSubmit} style={{ display: "flex", flexDirection: "column", gap: "12px", maxWidth: "400px" }}>
          <div>
            <label htmlFor="amount">Enter Amount:</label>
            <input
              id="amount"
              type="number"
              value={amount}
              onChange={(e) => setAmount(e.target.value)}
              required
              style={{ width: "100%", padding: "8px", marginTop: "4px" }}
            />
          </div>

          <div>
            <label htmlFor="description">Enter Description:</label>
            <input
              id="description"
              type="text"
              value={description}
              onChange={(e) => setDescription(e.target.value)}
              required
              style={{ width: "100%", padding: "8px", marginTop: "4px" }}
            />
          </div>

          <div>
            <label htmlFor="date">Enter Date:</label>
            <input
              id="date"
              type="datetime-local"
              value={date}
              onChange={(e) => setDate(e.target.value)}
              required
              style={{ width: "100%", padding: "8px", marginTop: "4px" }}
            />
          </div>

          <div>

            <label htmlFor="category">Choose an category:</label>
            <select id="category" value={category}
              onChange={(e) => setCategory(e.target.value)}
              required
              style={{ width: "100%", padding: "8px", marginTop: "4px" }}
            >
              <option value="3">Travel</option>
              <option value="4">Bank</option>
              <option value="5">Office Supplies</option>
            </select>
          </div>


          <button type="submit" style={{ padding: "10px 16px", marginTop: "12px", cursor: "pointer" }}>
            Submit
          </button>
        </form>
      </div>
    </div>
  );
}