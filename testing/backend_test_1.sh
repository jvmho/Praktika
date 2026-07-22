echo "Starting basig Auth + GET requests testing..."
echo ""

curl -s -X POST http://localhost:8080/api/v1/login/auth \
-H "Content-Type: application/json" \
-d '{"login":"admin","password":"admin"}' | jq

echo ""

TOKEN=$(curl -s -X POST http://localhost:8080/api/v1/login/auth \
-H "Content-Type: application/json" \
-d '{"login":"admin","password":"admin"}' | jq -r ".token")

echo ""
echo "Authenification ended"
echo ""

echo "All users from /api/v1/users/..."

curl -s -X GET http://localhost:8080/api/v1/users \
-H "Authorization: Bearer $TOKEN" | jq
 
echo ""
echo "Request ended"
echo ""

echo "User by {id} 1 from /api/v1/users/..."

curl -s -X GET http://localhost:8080/api/v1/users/1 \
-H "Authorization: Bearer $TOKEN" | jq

echo ""
echo "Request ended"
echo ""

echo "All drugs from /api/v1/drugs/..."

curl -s -X GET "http://localhost:8080/api/v1/drugs" \
-H "Authorization: Bearer $TOKEN" | jq

echo ""
echo "Request ended"
echo ""

echo "Test ended."
