package org.openbel.framework.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.openbel.framework.ws.model.GetCatalogResponse;
import org.openbel.framework.ws.model.Kam;
import org.openbel.framework.ws.model.ObjectFactory;
import org.openbel.framework.ws.service.KamStoreService;
import org.openbel.framework.ws.service.KamStoreServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Path("kams")
public class KamResource {

    /*
    @Autowired(required = true)
    private KAMCatalogDao kamCatalogDao;
    */

    @Autowired
    private KamStoreService kamStoreService;

    @GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public GetCatalogResponse getKams() throws KamStoreServiceException {
        GetCatalogResponse response = ObjectFactory.createGetCatalogResponse();
        for (Kam kam : kamStoreService.getCatalog()) {
            response.getKams().add(kam);
        }

        return response;
    }
    
    /*
    private List<Kam> getCatalog() {
        List<Kam> list = new ArrayList<Kam>();
        try {
            for (KamInfo kamInfo : kamCatalogDao.getCatalog()) {
                list.add(convert(kamInfo));
            }
        } catch (SQLException e) {
            // logger.warn(e.getMessage());
            // throw new KamStoreServiceException(e.getMessage());
        }
        return list;
    }
    */


    /*
    @GET
    @Path("{kamid}")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public void getKam(@PathParam("kamid") int kamId) {

    }
    */
    
    public void setKamStoreService(KamStoreService kamStoreService) {
        this.kamStoreService = kamStoreService;
    }
}
