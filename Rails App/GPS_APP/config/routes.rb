Rails.application.routes.draw do
	post '/create' => 'post#create'
	get '/get_fcm_token' => 'get#get_fcm_token'
	get '/check_login' => 'get#check_login'
end
