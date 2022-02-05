        stage('Create Infrastructure for the App') {
            steps {
                echo 'Creating Infrastructure for the App on AWS Cloud'
                sh 'terraform init'
                sh 'terraform apply --auto-approve'
                script {

                    instance_id = sh(script: 'aws ec2 describe-instances --filters "Name=tag-value,Values=docker-grand-master" --query Reservations[*].Instances[*].[InstanceId] --output text',  returnStdout:true).trim()
                    sh 'aws ec2 wait instance-status-ok --instance-ids $id'
                    
                    env.MASTER_INSTANCE_PUBLIC_IP = sh(script:'aws ec2 describe-instances --region ${AWS_REGION} --filters Name=tag-value,Values=docker-grand-master Name=instance-state-name,Values=running --query Reservations[*].Instances[*].[PublicIpAddress] --output text | sed "s/\\s*None\\s*//g"', returnStdout:true).trim()  
                   
                }
            }
        }