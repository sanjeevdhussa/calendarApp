package com.cagataygurturk.example.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.cagataygurturk.example.config.SpringConfig;
import com.cagataygurturk.example.model.ApptBookingEvent;
import com.cagataygurturk.example.services.Service;

import java.util.List;

import org.apache.log4j.Logger;

/**
 * Handler class should extend AbstractHandler<T> abstract class
 * T should be a Spring @Configuration class for Spring DI manager
 */
@SuppressWarnings("unused")
public class MainHandler
        extends AbstractHandler<SpringConfig>
        implements RequestHandler<MainHandler.Request, List<ApptBookingEvent>> {

    public MainHandler() {
        /**
         * Set example event for local running
         */
        exampleEvent = "{\"startDate\":\"06/01/2017\",\"endDate\":\"06/01/2017\", \"serviceType\":\"physio\",\"providerId\":\"1\"}";
    }

    /**
     * Standard logger. For logger configuration check resources/log4j.properties file.
     *
     * @link http://docs.aws.amazon.com/lambda/latest/dg/java-logging.html
     */
    static final Logger log = Logger.getLogger(MainHandler.class);


    /**
     * Request class is a POJO. You should modify this class according to your needs.
     * <p>
     * Event json that lambda function got is automatically serialized to this POJO. For more details see Lambda documentation:
     *
     * @link http://docs.aws.amazon.com/lambda/latest/dg/java-handler-io-type-pojo.html
     */
    public static class Request {
    	String startDate;
		String endDate;
    	String serviceType;
    	String providerId;
    	
//        String firstName;
//        String lastName;
//
//        public String getFirstName() {
//            return firstName;
//        }
//
//        public void setFirstName(String firstName) {
//            this.firstName = firstName;
//        }
//
//        public String getLastName() {
//            return lastName;
//        }
//
//        public void setLastName(String lastName) {
//            this.lastName = lastName;
//        }

//        public Request(String firstName, String lastName) {
//            this.firstName = firstName;
//            this.lastName = lastName;
//        }

        public Request() {
        }
        
        public Request(String startDate, String endDate, String serviceType, String providerId) {
			super();
			this.startDate = startDate;
			this.endDate = endDate;
			this.serviceType = serviceType;
			this.providerId = providerId;
		}

		public String getStartDate() {
			return startDate;
		}

		public void setStartDate(String startDate) {
			this.startDate = startDate;
		}

		public String getEndDate() {
			return endDate;
		}

		public void setEndDate(String endDate) {
			this.endDate = endDate;
		}

		public String getServiceType() {
			return serviceType;
		}

		public void setServiceType(String serviceType) {
			this.serviceType = serviceType;
		}

		public String getProviderId() {
			return providerId;
		}

		public void setProviderId(String providerId) {
			this.providerId = providerId;
		}
    }

    /**
     * Response is also a POJO that will handler return and
     * Lambda runtime automatically serializes it to JSON. Again see the documentation.
     *
     * @link http://docs.aws.amazon.com/lambda/latest/dg/java-handler-io-type-pojo.html
     */
//    public static class Response {
//        String greetings;
//
//        public String getGreetings() {
//            return greetings;
//        }
//
//        public void setGreetings(String greetings) {
//            this.greetings = greetings;
//        }
//
//        public Response(String greetings) {
//            this.greetings = greetings;
//        }
//
//        public Response() {
//        }
//
//    }


    /**
     * Main handler method is invoked when Lambda function is invoked. You should configure the name of this method in the AWS Console.
     * In this example, the value would be com.cagataygurturk.lambda.MainHandler. As we implement RequestHandler interface Lambda runtime
     * detects this method automatically and invokes it.
     * <p>
     * <p>
     * <p>
     * As a best practice, this method should be kept very short and all the business logic should sit
     * in "Service" instance that we will fetch from Spring IoC container and will enjoy from all
     * Spring IoC features.
     * <p>
     * Lambda specific code ends here and beginning from this point old good JAVA starts.
     *
     * @param request Request object
     * @param context Context object
     * @return Response
     * @throws RuntimeException
     * @see RequestHandler
     */
    public List<ApptBookingEvent> handleRequest(Request request, Context context)
            throws RuntimeException {

        /**
         * BusinessService is where all our business logic sits.
         */
        Service businessService = getApplicationContext().getBean(Service.class);
        return businessService.getBookings( request.startDate, request.endDate, request.serviceType, request.providerId);
    }
}
