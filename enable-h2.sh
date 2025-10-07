#!/usr/bin/env bash
set -euo pipefail
echo "cwd: $(pwd)"

# เช็คว่ามี pom.xml จริงไหม
if [ ! -f "pom.xml" ]; then
  echo "❌ pom.xml not found in: $(pwd)"
  exit 1
fi

# เพิ่ม dependency H2 ถ้ายังไม่มี
if ! grep -q "com.h2database" pom.xml; then
  awk '1; /<dependencies>/ && !x {
      print "    <dependency>";
      print "      <groupId>com.h2database</groupId>";
      print "      <artifactId>h2</artifactId>";
      print "      <scope>runtime</scope>";
      print "    </dependency>";
      x=1
  }' pom.xml > pom.xml.new && mv pom.xml.new pom.xml
  echo "✅ Added H2 dependency to pom.xml"
else
  echo "ℹ️  H2 dependency already exists"
fi

# เพิ่ม datasource config (append ไว้ท้ายไฟล์)
APP_PROPS="src/main/resources/application.properties"
mkdir -p "$(dirname "$APP_PROPS")"

# กันซ้ำ: ถ้ามีอยู่แล้วจะไม่ใส่ซ้ำ
if ! grep -q "jdbc:h2:mem:testdb" "$APP_PROPS" 2>/dev/null; then
  cat >> "$APP_PROPS" <<'EOP'
# === H2 Database (for local dev) ===
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
EOP
  echo "✅ Added H2 datasource config to $APP_PROPS"
else
  echo "ℹ️  H2 config already present in $APP_PROPS"
fi
