# Sử dụng môi trường Java 17 bản siêu nhẹ
FROM openjdk:17-jdk-slim

# Thiết lập thư mục làm việc trong Container
WORKDIR /app

# Copy toàn bộ mã nguồn vào Container
COPY . .

# Cấp quyền và đóng gói ứng dụng thành file .jar
RUN chmod +x gradlew
RUN ./gradlew build -x test

# Mở cổng 8080 cho Backend
EXPOSE 8080

# Khởi chạy file .jar khi Container bắt đầu
CMD sh -c 'java -jar build/libs/*.jar'