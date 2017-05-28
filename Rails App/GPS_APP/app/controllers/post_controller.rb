class PostController < ApplicationController
	protect_from_forgery with: :null_session
	def create
		if User.exists?(username: params[:username])
			render text: "username_taken" and return
		elsif	User.exists?(tracker_id: params[:tracker_id])
			render text: "tracker_id_taken" and return
		else
			@user = User.new
			@user.username = params[:username]
			@user.password = params[:password]
			@user.tracker_id = params[:tracker_id]
			@user.fcm_token = params[:fcm_token]
			@user.save
			render text: "success"
		end
	end

end
