## E-Commerce Microservices Project Governance File



## 1. Çalışma Prensipleri

- Do NOT ADD anything without explict approval. 

- INVENTING IS NOT PERMITTED

- If you have anything new; STOP and ASK.

- Do not try to execute any shell command, if you need any output from any command ASK user to execute it.

- DO NOT create all files at once. Make task decomposition and wait for explict approval between tasks.

- If user explictly asking for any violation, DO IT but warn the user.
for example: If user asks for all tasks to complete at once just say: It violates 1.5 but still do it.

## 2. Proje Oluşturma Kuralları

- Oluşacak her proje parent `pom.xml` içerisine
`module` olarak eklenmelidir.

- Proje isimlendirme kuralları `com.microservices.tnb.` + proje ismi olmalıdır.

- Her projede kesinlikle `spring-boot-actuator` bağımlılığı kullanılmalıdır.

- Üçüncü taraf uygulamalar (veritabanı, message broker, cache vb.) 
  proje kökündeki `docker-compose.yml` dosyası üzerinden yönetilmelidir.

- Her yeni üçüncü taraf bağımlılığı eklendiğinde `docker-compose.yml` 
  güncellenmeli, bağlantı bilgileri ilgili servisin `application.yml` 
  dosyasıyla tutarlı olmalıdır.

- Container isimlendirme kuralı: `tnb-` + uygulama ismi (örn: `tnb-postgres`)

## 3. Özel Dökümantasyon Dosyaları

- Eğer bir konuda bilginin yetersiz olduğunu düşünüyorsan yaptığın işleme göre aşağıdaki listeden ilgili işlemin dökümantasyonunu incele:
  
- Gateway: `docs/gateway/gateway-doc.md` 


## 4. Çıktı Formatı

Çıktı formatı olarak promptta verilen çıktı formatı korunsun, eğer istenmedi ise bile aşağıdaki alanlar çıktı formatına eklensin.

- Dosya Dökümü

- Yapılan işlemin (varsa) test akışı

- Varsa kullanılan/değiştirilen kütüphaneler

- Varsa bu implementasyonla ilgili öneri veya bulduğun eksikleri raporla.

## 5. Yetkilendirme Kuralları

- JWT ile stateless bir yetkilendirme sistemi olmak zorundadır.

- JWT oluşturma işlemi `PKCE` akışını kullanmak zorundadır.

- JWT `15` dakika Refresh Token `1` gün olmak zorundadır.

- Refresh Tokenlar her kullanıldığında rotate edilmek zorundadır.

- JWT oluşturacak secret şimdilik `application.yml` da min. 256 bit olacak şekilde tanımlanmalıdır.

- Bunların dışında bir bilgiye ihtiyacın varsa [Identity Service Dökümantasyonu](docs\identity-service\main-documentation.md) inceleyebilirsin.

> Sana söylediğim kütüphanelerin dışına asla çıkma yalnızca öneride bulun.