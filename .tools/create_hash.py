import bcrypt
import getpass

password = getpass.getpass("Mot de passe à hasher : ").encode("utf-8")

hashed = bcrypt.hashpw(
    password,
    bcrypt.gensalt(rounds=10)
)

print("\nHash BCrypt :")
print(hashed.decode("utf-8"))
