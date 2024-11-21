# Start the application

```bash
./mvnw spring-boot:run
```

# CRUD examples using curl

## Products

### Create product

```bash
curl -v -H "Content-Type: application/json" -d '{"code":"A0001","price":12.99}' http://localhost:8080/api/products
```

### Read product(s)

```bash
curl -v http://localhost:8080/api/products
curl -v http://localhost:8080/api/products/1
```

### Update product

```bash
curl -v -X PUT -H "Content-Type: application/json" -d '{"code":"A0001","price":9.99}' http://localhost:8080/api/products/1
```

### Delete product

```bash
curl -v -X DELETE http://localhost:8080/api/products/1
```

## Discounts

### Create discount

```bash
curl -v -H "Content-Type: application/json" -d '{"type":"TEN_PERCENT_OFF","productCode":"A0001"}' http://localhost:8080/api/discounts
```

### Read discount(s)

```bash
curl -v http://localhost:8080/api/discounts
curl -v http://localhost:8080/api/discounts/1
```

### Update discount

```bash
curl -v -X PUT -H "Content-Type: application/json" -d '{"type":"BUY_1_GET_1_FREE","productCode":"A0001"}' http://localhost:8080/api/discounts/1
```

### Delete discount

```bash
curl -v -X DELETE http://localhost:8080/api/discounts/1
```

## Baskets

### Create basket

```bash
curl -v -H "Content-Type: application/json" -d '{}' http://localhost:8080/api/baskets
```

### Read basket(s)

```bash
curl -v http://localhost:8080/api/baskets
curl -v http://localhost:8080/api/baskets/1
```

### Add product (a.k.a. "scan")

```bash
curl -v -X PATCH http://localhost:8080/api/baskets/1/products/1
curl -v -X PATCH http://localhost:8080/api/baskets/1/scan/A0001
```

### Remove product

```bash
curl -v -X DELETE http://localhost:8080/api/baskets/1/products/1
```

### Add discount

```bash
curl -v -X PATCH http://localhost:8080/api/baskets/1/discounts/1
```

### Remove discount

```bash
curl -v -X DELETE http://localhost:8080/api/baskets/1/discounts/1
```

### Delete basket

```bash
curl -v -X DELETE http://localhost:8080/api/discounts/1
```

### Get total

```bash
curl -v http://localhost:8080/api/baskets/1/total
```

# Run tests

```bash
./mvnw verify
```
