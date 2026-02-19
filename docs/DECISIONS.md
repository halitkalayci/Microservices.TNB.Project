### DECISIONS.md

Son Güncellenme: 2026-02-19

--- 
Bu dosya tüm projeler için teknoloji ve mimarisel kararların kesinleştirildiği dosyadır.

---

### ADR-001: Veritabanı Seçimi

Son Güncellenme: 2026-02-19

- Durum: Karar kesinleştirdi.

- Karar: PostgreSQL

- Sebep: Açık kaynak olmasından ötürü ücret politikasının kuruma daha uygun olması.

- Alternatifler: 
   - MySQL
   - Oracle

---

### ADR-002: Service Registry (Discovery)

Son Güncellenme: 2026-02-19

- Durum: Karar kesinleştirdi.

- Karar: Netflix Eureka (Spring Cloud Netflix) ile merkezi Service Registry kullanılacak.

- Sebep:
   - Mikroservislerin dinamik olarak keşfedilmesi (service discovery) ve adres yönetiminin merkezi olması.
   - Spring ekosistemi ile starter bazlı hızlı entegrasyon ve operasyonel basitlik.
   - Mevcut Spring Boot 3.5.x sürümü ile uyumlu Spring Cloud release train (2025.0.x / Northfields) üzerinden yönetilebilir olması.

- Alternatifler:
   - HashiCorp Consul (Spring Cloud Consul)
   - Apache Zookeeper (Spring Cloud Zookeeper)
   - Kubernetes native discovery (Spring Cloud Kubernetes)
