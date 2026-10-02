import re

with open('app/src/main/java/com/example/ui/components/PaymentQRDialog.kt', 'r') as f:
    content = f.read()

# Change "Start Ride" to "Complete Delivery"
content = content.replace('Text("Start Ride"', 'Text("Complete Delivery"')

with open('app/src/main/java/com/example/ui/components/PaymentQRDialog.kt', 'w') as f:
    f.write(content)
