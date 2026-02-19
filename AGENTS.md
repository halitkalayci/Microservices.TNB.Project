## E-Commerce Microservices Project Governance File



## 1. Çalışma Prensipleri

- Do NOT ADD anything without explict approval. 

- INVENTING IS NOT PERMITTED

- If you have anything new; STOP and ASK.

- Do not try to execute any shell command, if you need any output from any command ASK user to execute it.

## 2. Proje Oluşturma Kuralları

- Oluşacak her proje parent `pom.xml` içerisine
`module` olarak eklenmelidir.

- Proje isimlendirme kuralları `com.microservices.tnb.` + proje ismi olmalıdır.

## 3. Özel Dökümantasyon Dosyaları

- Eğer bir konuda bilginin yetersiz olduğunu düşünüyorsan yaptığın işleme göre aşağıdaki listeden ilgili işlemin dökümantasyonunu incele:
  
- Gateway: `docs/gateway/gateway-doc.md` 


## 4. Çıktı Formatı

Çıktı formatı olarak promptta verilen çıktı formatı korunsun, eğer istenmedi ise bile aşağıdaki alanlar çıktı formatına eklensin.

- Dosya Dökümü

- Yapılan işlemin (varsa) test akışı

- Varsa kullanılan/değiştirilen kütüphaneler