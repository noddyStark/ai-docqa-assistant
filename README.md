# AI-Powered Documentation Q&A Assistant

An AI-powered backend application that ingests documentation, chunks content, generates embeddings, stores vectors in PostgreSQL with pgvector, retrieves semantically relevant chunks, and produces grounded answers with source attribution.

## Features

- Upload `.txt`, `.md`, and `.pdf` documents
- OCR fallback for scanned PDFs
- Chunk document text with overlap
- Generate embeddings using OpenAI API
- Store embeddings in PostgreSQL with pgvector
- Retrieve top relevant chunks using vector similarity search
- Ask grounded questions over ingested documents
- Return source-attributed answers
- Swagger/OpenAPI documentation
- Simple frontend UI for upload, search, and Q&A

## Tech Stack

- Java 21+ / Spring Boot
- PostgreSQL
- pgvector
- OpenAI API
- Apache PDFBox
- Tess4J / Tesseract OCR
- Docker
- Swagger / OpenAPI
- HTML / CSS / JavaScript

## Architecture

1. User uploads a document
2. Backend extracts text
3. Content is normalized and chunked
4. Embeddings are generated for each chunk
5. Chunks and vectors are stored in PostgreSQL
6. User asks a question
7. Question embedding is generated
8. Most relevant chunks are retrieved using pgvector similarity search
9. Retrieved chunks are passed to the LLM
10. Grounded answer with sources is returned

## Main APIs

- `POST /api/documents` — ingest raw JSON document
- `POST /api/documents/upload` — upload file
- `GET /api/documents` — document library summary
- `GET /api/documents/{documentId}` — document detail with chunk summaries
- `GET /api/documents/{documentId}/chunks` — full chunk list
- `PUT /api/documents/{documentId}` — re-ingest/update document
- `DELETE /api/documents/{documentId}` — delete document and chunks
- `POST /api/documents/search` — semantic chunk retrieval
- `POST /api/documents/ask` — grounded Q&A
- `GET /health` — health check

## Local Setup

### 1. Clone repo
```bash
git clone <your-repo-url>
cd ai-docqa-assistant
```

### 2. Start PostgreSQL with pgvector
```bash
docker compose up -d
```
### 3. Enable pgvector extension
```
docker exec -it docqa-postgres psql -U docqa -d docqa
CREATE EXTENSION IF NOT EXISTS vector;
\q
```

### 4. Configure environment variables
Set OPENAI_API_KEY in your IntelliJ run configuration.

Optional OCR config in application.properties:
```
ocr.tesseract.datapath=/opt/homebrew/share/tessdata
```

### 5. Run the Spring Boot app
Use IntelliJ or:
```
./mvnw spring-boot:run
```

### 6. Open the app
   * Frontend UI: http://localhost:8080/
   * Swagger UI: http://localhost:8080/swagger-ui/index.html