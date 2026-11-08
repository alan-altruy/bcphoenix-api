async function login() {
    try {
        const email = prompt("Adresse email :");

        if (email === null || !email.trim()) {
            console.log("Connexion annulée.");
            return;
        }

        const password = prompt("Mot de passe :");

        if (password === null) {
            console.log("Connexion annulée.");
            return;
        }

        // Récupération du cookie CSRF
        await fetch("http://localhost:8080/", {
            credentials: "include"
        });

        const csrfToken = document.cookie
            .split("; ")
            .find(row => row.startsWith("XSRF-TOKEN="))
            ?.split("=")[1];

        if (!csrfToken) {
            throw new Error("Token CSRF introuvable");
        }

        const response = await fetch("http://localhost:8080/api/auth/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded",
                "X-XSRF-TOKEN": decodeURIComponent(csrfToken)
            },
            body: new URLSearchParams({
                email: email.trim(),
                password: password
            }),
            credentials: "include"
        });

        if (response.status === 204) {
            console.log("Connexion réussie !");
        } else {
            console.error("Échec :", response.status, await response.text());
        }

    } catch (error) {
        console.error("Erreur :", error);
    }
}

login();