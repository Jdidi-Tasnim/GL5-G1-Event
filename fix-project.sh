#!/bin/bash
echo "🚀 APPLICATION DES CORRECTIONS AUTOMATIQUES"

# 1. Créer les fichiers manquants
echo "📝 Création des fichiers manquants..."
cat > k8s/namespace.yaml << 'EOF'
apiVersion: v1
kind: Namespace
metadata:
  name: gl5-g1-production
  labels:
    name: gl5-g1-production
    environment: production
EOF

cat > k8s/hpa.yaml << 'EOF'
apiVersion: autoscaling/v2
kind: HorizontalPodAutoscaler
metadata:
  name: events-hpa
  namespace: gl5-g1-production
spec:
  scaleTargetRef:
    apiVersion: apps/v1
    kind: Deployment
    name: events-application
  minReplicas: 2
  maxReplicas: 5
  metrics:
  - type: Resource
    resource:
      name: cpu
      target:
        type: Utilization
        averageUtilization: 50
EOF

# 2. Corriger le deployment
echo "🔧 Correction du deployment..."
cat > k8s/deployment.yaml << 'EOF'
apiVersion: apps/v1
kind: Deployment
metadata:
  name: events-application
  namespace: gl5-g1-production
  labels:
    app: events
spec:
  replicas: 2
  selector:
    matchLabels:
      app: events
  template:
    metadata:
      labels:
        app: events
    spec:
      containers:
      - name: events-app
        image: khalilsmida/docker-spring-boot:latest
        ports:
        - containerPort: 8080
        env:
        - name: SPRING_PROFILES_ACTIVE
          value: "prod"
        resources:
          requests:
            memory: "256Mi"
            cpu: "250m"
          limits:
            memory: "512Mi"
            cpu: "500m"
        livenessProbe:
          httpGet:
            path: /actuator/health
            port: 8080
          initialDelaySeconds: 30
          periodSeconds: 10
        readinessProbe:
          httpGet:
            path: /actuator/health
            port: 8080
          initialDelaySeconds: 20
          periodSeconds: 5
EOF

# 3. Corriger le service
echo "🔧 Correction du service..."
cat > k8s/service.yaml << 'EOF'
apiVersion: v1
kind: Service
metadata:
  name: events-service
  namespace: gl5-g1-production
  labels:
    app: events
spec:
  selector:
    app: events
  ports:
    - protocol: TCP
      port: 80
      targetPort: 8080
  type: LoadBalancer
EOF

echo "✅ Corrections appliquées avec succès!"