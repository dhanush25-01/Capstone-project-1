# DhanushMart AI Image Search

## What was added
- Upload a men's clothing image from the Products page.
- AI identifies product type, color, style, material and pattern.
- The generated shopping keywords are matched against the DhanushMart product database.
- Matching products are displayed without changing the existing catalogue.

## Enable AI
The feature uses an OpenAI vision-capable model through the Chat Completions API.

### Windows PowerShell
```powershell
$env:OPENAI_API_KEY="YOUR_API_KEY"
mvn spring-boot:run
```

### Or application.properties
Set:
```properties
openai.api.key=YOUR_API_KEY
openai.model=gpt-4o-mini
```
Using an environment variable is safer because it keeps the key out of source control.

Open `http://localhost:8080/products`, choose an image, and click **Search by Image**.

## Without an API key
The application still runs normally. The AI Image Search screen will tell you that an API key is required instead of failing the whole application.

## Notes
- Maximum image upload size is 10 MB.
- The AI does not invent product IDs, prices, brands or sizes.
- Results come only from the products stored in your DhanushMart database.
