task default: %w[tvnexa:deploy]

namespace :tvnexa do

	desc "Authenticating with existing credentials"
	task :login do
		puts `docker login 2>&1`
	end

	desc "Cleaning Environment Task"
	task :cleaning_environment_task do
		puts "Cleaning Environment"
		puts `docker image prune -af`
		puts `docker volume prune -f 2>&1`
	end

	desc "Status Containers"
	task :status => [
         "redis:status",
         "galera:status",
         "platform:status"
        ] do
		puts "Show Containers Status"
	end

	desc "Deploys Platform Containers and launches all services and daemons needed to properly work"
	task :deploy => [
		:cleaning_environment_task,
		"redis:start",
		"galera:start",
		"platform:start",
		:status] do
	    puts "Deploying services..."
	end

	desc "Undeploy Platform Containers"
	task :undeploy => [ :status ] do
		puts "Undeploy Services"
		puts `docker-compose down -v 2>&1`
	end

	desc "Check Docker and Docker Compose Task"
	task :check_docker_task do
		puts "Check Docker and Docker Compose ..."
		if which('docker') && which('docker-compose')
			show_docker_version
			show_docker_compose_version
		else
			raise "Please check that Docker and Docker Compose are visible and accessible in the PATH"
		end
	end

    ## Deploy MariaDB Galera Cluster with HAProxy
	namespace :galera do

	    desc "Check Platform Deployment File"
    	task :check_deployment_file do
    		puts "Check Platform Deployment File ..."
    		raise "Deployment file not found, please check availability" unless File.file?("./mariadb_galera_cluster/docker-compose.yml")
    		puts "Platform Deployment File OK!"
    	end

    	desc "Start MariaDB Galera Cluster and HAProxy containers"
    	task :start => [ :check_docker_task, :login, :check_deployment_file ] do
    	    puts "Start MariaDB Galera Cluster and HAProxy containers"
            puts `docker-compose -f ./mariadb_galera_cluster/docker-compose.yml up -d`
        end

        desc "Stop MariaDB Galera Cluster and HAProxy containers"
        task :stop => [ :check_docker_task, :login, :check_deployment_file  ] do
            puts "Stop Platform Containers"
            puts `docker-compose -f ./mariadb_galera_cluster/docker-compose.yml stop 2>&1`
        end

        desc "Check Status of MariaDB Galera Cluster and HAProxy containers"
        task :status => [ :check_docker_task, :login, :check_deployment_file ] do
            puts "Check Status of MariaDB Galera Cluster and HAProxy containers"
            puts `docker-compose -f ./mariadb_galera_cluster/docker-compose.yml ps`
        end
	end

	# Redis Cluster
    namespace :redis do

    	desc "Check Redis Cluster Deployment File"
    	task :check_deployment_file do
    		puts "Check Redis Cluster Deployment File ..."
    		raise "Deployment file not found, please check availability" unless File.file?("./redis_cluster/docker-compose.yml")
    		puts "Platform Deployment File OK!"
    	end

    	desc "Start and configure Cluster Containers"
    	task :start => [ :check_docker_task, :login, :check_deployment_file ] do
    		puts "Start Cluster Containers"
    		puts `docker-compose -f ./redis_cluster/docker-compose.yml up -d`
    		puts `docker run -it --rm --network=redis_cluster_redis_cluster_network redislabs/rejson:latest redis-cli --cluster create 192.168.0.30:6379 192.168.0.35:6380 192.168.0.40:6381 192.168.0.45:6382 192.168.0.50:6383 192.168.0.55:6384 192.168.0.60:6385 192.168.0.65:6386 --cluster-replicas 1 --cluster-yes`
    	end

    	desc "Stop Cluster Containers"
    	task :stop => [ :check_docker_task, :login, :check_deployment_file  ] do
    		puts "Stop Cluster Containers"
    		puts `docker-compose -f ./redis_cluster/docker-compose.yml stop 2>&1`
    	end

    	 desc "Check Status of Redis Cluster Containers"
         task :status => [ :check_docker_task, :login, :check_deployment_file ] do
             puts "Check Status of Redis Cluster Containers"
             puts `docker-compose -f ./redis_cluster/docker-compose.yml ps`
         end

    end

	## Deploy Platform
	namespace :platform do

		desc "Check Platform Deployment File"
		task :check_deployment_file do
			puts "Check Platform Deployment File ..."
			raise "Deployment file not found, please check availability" unless File.file?("./platform/docker-compose.yml")
			puts "Platform Deployment File OK!"
		end

		desc "Start Platform Hotspot JVM Containers"
		task :start => [ :check_docker_task, :login, :check_deployment_file ] do
			puts "Start Platform Containers"
			puts `docker-compose -f ./platform/docker-compose.yml up -d`
		end

		desc "Stop Platform Hotspot JVM Containers"
		task :stop => [ :check_docker_task, :login, :check_deployment_file  ] do
			puts "Stop Platform Containers"
			puts `docker-compose -f ./platform/docker-compose.yml stop 2>&1`
		end

		desc "Build Docker Image based on Hotspot JVM"
		task :build_image => [:check_docker_task, :login] do
		    microservicesFolder = "./platform/microservices"
			apiServiceDockerImage = "ssanchez11/tv_nexa_api_service:0.0.1"
			ingestionServiceDockerImage = "ssanchez11/tv_nexa_ingestion_service:0.0.1"
			puts "Build Docker Image #{apiServiceDockerImage} based on Hotspot"
			puts `docker build -t #{apiServiceDockerImage} -f #{microservicesFolder}/tv_nexa_api_service/Dockerfile #{microservicesFolder}`
			puts "Docker image #{apiServiceDockerImage} has been created! trying to upload it!"
			puts `docker push #{apiServiceDockerImage}`
			puts "Build Docker Image #{ingestionServiceDockerImage} based on Hotspot"
            puts `docker build -t #{ingestionServiceDockerImage} -f #{microservicesFolder}/tv_nexa_ingestion_service/Dockerfile #{microservicesFolder}`
            puts "Docker image #{ingestionServiceDockerImage} has been created! trying to upload it!"
            puts `docker push #{ingestionServiceDockerImage}`
			puts `docker images`
		end

		desc "Check Status of Platform Containers"
        task :status => [ :check_docker_task, :login, :check_deployment_file ] do
            puts "Check Status of Platform Containers"
            puts `docker-compose -f ./platform/docker-compose.yml ps`
        end
	end


	## Utils Functions

	def show_docker_version
	  puts `docker version 2>&1`
	end

	def show_docker_compose_version
	  puts `docker-compose version 2>&1`
	end

	# Cross-platform way of finding an executable in the $PATH.
	# which('ruby') #=> /usr/bin/ruby
	def which(cmd)
	  exts = ENV['PATHEXT'] ? ENV['PATHEXT'].split(';') : ['']
	  ENV['PATH'].split(File::PATH_SEPARATOR).each do |path|
	    exts.each { |ext|
	      exe = File.join(path, "#{cmd}#{ext}")
	      return exe if File.executable?(exe) && !File.directory?(exe)
	    }
	  end
	  return nil
	end

end
