import { useEffect, useState } from "react";
import { useRouter } from "next/router";
import styles from "../styles/dashboard.module.css";

export default function Dashboard() {
  const router = useRouter();
  const [user, setUser] = useState(null);
  const [expenses, setExpenses] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [total_expenses, setTotalExpenses] =useState({});
  const  categories =  {
     1: "Travel",
     2: "Bank",
     3: "Office Supplies "
  };


  useEffect(() => {
    console.log(expenses);    
    const currentTotal = {
      // ...total_expenses
    }; 
    expenses.forEach(
      expense => {
        const catId = expense.categoryId;
        const categoryName = categories[catId];
        if(!currentTotal[categoryName]){
          currentTotal[categoryName] = expense.amount;
        }
        else{
          currentTotal[categoryName] = currentTotal[categoryName]+ expense.amount;
        }
      }
    )
    console.log(currentTotal);
    setTotalExpenses(currentTotal);
  }, [expenses]);

  const fetchExpenses = async () => {
      setLoading(true);
      setError("");
      try {
            
          const params = { userId: JSON.parse(localStorage.getItem("user")).id };
          const url = new URL('http://localhost:8080/api/expenses');
          Object.keys(params).forEach(key => url.searchParams.append(key, params[key]));

          const res = await fetch(url, {
          method: "GET",
          headers: { "Content-Type": "application/json" },
         
        });
        console.log("RES", res)
        if (!res.ok) 
          throw new Error("Failed to fetch expenses");
        const data = await res.json();
        setExpenses(data);
      } catch (err) {
        console.error("Failed to fetch expenses:", err);
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };
  useEffect(() => {
    const storedUser = localStorage.getItem("user");
    if (!storedUser) {
      router.push("/"); // redirect if not logged in
      return;
    }

    const parsedUser = JSON.parse(storedUser);
    setUser(parsedUser);

    fetchExpenses();
  }, [router]);
  const handleLogout = () => {
    localStorage.removeItem("user");
    router.push("/");
  };

  const handleAddExpense = () => {
    router.push("/create_expense");
  };

  if (!user) return <div className={styles.container}>Loading...</div>;

  return (
    <div className={styles.container}>
      <h1>
        Welcome, {user.name}! <br />
        You are logged in as <strong>{user.role}</strong>
      </h1>

      <button onClick={handleAddExpense} className={styles.logoutButton}>
        Add Expense
      </button>
      <br></br>
      <button onClick={handleLogout} className={styles.logoutButton}>
        Logout
      </button>
      

      
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
                    <td>{exp.description}</td>
                    <td>{exp.amount}</td>
                    <td>{new Date(exp.date).toLocaleDateString()}</td>
                    <td>{exp.status}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>
          <br></br>
          <h2>Total Expenses by Category</h2>
        <div>
          <table className={styles.expensesTable}>
              <thead>
                <tr>
                  <th>category</th>
                  <th>Total</th>
                </tr>
              </thead>
              <tbody>
                {Object.entries(total_expenses).map(([category, total_expenditure]) => (
                  <tr key={category}>
                    <td>{category}</td>
                    <td>{total_expenditure}</td>
                  </tr>
                ))}
              </tbody>
            </table>
        </div>
    </div>
  );
}