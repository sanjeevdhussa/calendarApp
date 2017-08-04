package com.cagataygurturk.example.services;

import java.io.File;
import java.io.IOException;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.cagataygurturk.example.model.ServiceProvider;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ServiceProvidersService {
	
	ServiceProvider [] serviceProviders;
	
	@PostConstruct
	public void init(){
		ObjectMapper mapper = new ObjectMapper();
		//JSON from file to Object
		try {
			//Get file from resources folder
			ClassLoader classLoader = getClass().getClassLoader();
			File file = new File(classLoader.getResource("provider_details.json").getFile());
			serviceProviders = mapper.readValue(file, ServiceProvider[].class);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ServiceProvider getProviderById(String providerId){
		if(providerId == null){
			return null;
		}
		for(ServiceProvider provider : serviceProviders){
			if(providerId.equals(provider.getProvideId())){
				return provider;
			}
		}
		return null;
	}
}
