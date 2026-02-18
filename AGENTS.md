## E-Commerce Microservices Project Governance File

## 1. Çalışma Prensipleri

- Do NOT ADD anything without explict approval. 

- INVENTING IS NOT PERMITTED

- If you have anything new; STOP and ASK.

## 2. Proje Oluşturma Kuralları

- Oluşacak her proje parent `pom.xml` içerisine
`module` olarak eklenmelidir.

- Proje isimlendirme kuralları `com.microservices.tnb.`+proje ismi olmalıdır.

- Bir endpoint oluşturman gerekiyorsa o servis için `docs/openapi/` kontrat dosyalarını ara. Eğer kontrat oluşturulmamış ise dur ve kullanıcıyı uyar.