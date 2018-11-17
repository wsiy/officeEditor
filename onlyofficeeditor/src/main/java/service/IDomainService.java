package main.java.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;



//import org.codehaus.jettison.json.JSONArray;
//import net.sf.json.JSONArray;

import main.java.entities.CoreDocument;
import main.java.entities.Document;
import main.java.entities.PageBeanCoreDocument;
import main.java.entities.PageBeanDocument;
import main.java.entities.User;
import net.sf.json.JSONArray;


@Path("/Domain")
public interface IDomainService {
	
	/*
	 * methods for User
	 */
    @GET
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/doLogin/{username}/{pwd}")
    Response doLogin(@PathParam("username") String username, @PathParam("pwd") String pwd);
    
    
    @GET
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/getAllUsers")
    List<User> getAllUsers();
    
	/*
	 * methods for DocumentInfo page
	 */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/getDocuments")
    List<Document> getDocuments();
    
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/getDocumentsByNameInCore/{fileName}")
    List<Document> getDocumentsByNameInCore(@PathParam("fileName") String fileName);
    
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/getDocumentsByName/{fileName}")
    PageBeanDocument getDocumentsByName(@PathParam("fileName") String fileName);
    
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/getDocumentsByAuthor/{author}")
    List<Document> getDocumentsByAuthor(@PathParam("author") String author);
    
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/getDocumentsByCreateDate/{createDate}")
    PageBeanDocument getDocumentsByCreateDate(@PathParam("createDate") Date createDate);
    
    
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/getDocumentsByType/{typeName}")
    PageBeanDocument getDocumentsByType(@PathParam("typeName") String typeName);

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/getDocumentsByTypeInCore/{fileName}")
    List<Document> getDocumentsByTypeInCore(@PathParam("fileName") String fileName);
    
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/getDocumentsByTimeOrder/{isAsc}")
    PageBeanDocument getDocumentsByTimeOrder( @PathParam("isAsc") Boolean isAsc);
    
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/deleteDocumentByID/{documentID}")
    Response deleteDocumentByID(@PathParam("documentID") int documentID);
    
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/collectDocumentByID/{documentID}")
    Response collectDocumentByID(@PathParam("documentID") int documentID);
    
	/**************************************************************************************************************************/
    /*
	 * methods for CoreDocumentInfo page
	 */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/getCoreDocuments")
    List<CoreDocument> getCoreDocuments();
    
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("/getCoreDocumentsByDate/{createDateEarliest}/{createDateLatest}")
    PageBeanCoreDocument getCoreDocumentsByDate(@PathParam("createDateEarliest") Date createDateEarliest,@PathParam("createDateLatest") Date createDateLatest);
    
    
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/getCoreDocumentsByType/{typeName}")
    PageBeanCoreDocument getCoreDocumentsByType(@PathParam("typeName") String typeName);
    
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/getCoreDocumentsByClassfication/{classficationName}")
    PageBeanCoreDocument getCoreDocumentsByClassfication(@PathParam("classficationName") String classficationName);
    
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/getCoreDocumentsByExpression/{expressionName}")
    PageBeanCoreDocument getCoreDocumentsByExpression(@PathParam("expressionName") String expressionName);
      
    
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/getCoreDocumentsByName/{fileName}")
    PageBeanCoreDocument getCoreDocumentsByName(@PathParam("fileName") String fileName);
    

    
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/addCoreDocument/{data}") 
    Response addCoreDocument(@PathParam("data") String data) throws ParseException;
    
 
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/deleteCoreDocumentByID/{coreDocumentID}")
    Response deleteCoreDocumentByID(@PathParam("coreDocumentID") int coreDocumentID);
    
    
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/getCoreDocumentsByTimeOrder/{isAsc}")
    PageBeanCoreDocument getCoreDocumentsByTimeOrder(@PathParam("isAsc") Boolean isAsc);
    
    /*
     * 在线查看
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/viewOnline/{fileName}")
    String viewOnline (@PathParam("fileName") String fileName);
    
	/**************************************************************************************************************************/
    /*
	 * methods for New File
	 */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/getDocumentsByNameType/{name}/{type}")
    List<Document> getDocumentsByNameType(@PathParam("name") String Name,@PathParam("type") String type);

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/getDocumentsByTypeAuthor/{type}/{author}")
    List<Document> getDocumentsByTypeAuthor(@PathParam("type") String type,@PathParam("author") String author);

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/getDocumentsByNameAuthor/{name}/{author}")
    List<Document> getDocumentsByNameAuthor(@PathParam("name") String name,@PathParam("author") String author);

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/getDocumentsByNameTypeAuthor/{name}/{type}/{author}")
    List<Document> getDocumentsByNameTypeAuthor(@PathParam("name") String name,@PathParam("type") String type,@PathParam("author") String author);


    
    /*
     * 初始分页--Document
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/findInitDocument")
    PageBeanDocument findInitDocument();
    
    /*
     * 分页--Document
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/findPartDocument/{totalList}")
    PageBeanDocument findPartDocument(@PathParam("totalList") List<Document> totalList);
        
    
    /*
     * 初始分页--CoreDocument
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/findInitCoreDocument")
    PageBeanCoreDocument findInitCoreDocument();
    
    /*
     * 分页--CoreDocument
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/findPartCoreDocument/{totalList}")
    PageBeanCoreDocument findPartCoreDocument(@PathParam("totalList") List<CoreDocument> totalList);
        
    
    /*
     * 新建Document
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/addDocument/{data}") 
    Response addDocument(@PathParam("data") String data) throws ParseException;


 }
