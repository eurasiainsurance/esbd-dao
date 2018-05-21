package tech.lapsa.esbd.beans.dao.entities.complex.converter;

import static tech.lapsa.esbd.beans.dao.entities.complex.Util.*;

import javax.ejb.EJB;

import com.lapsa.international.country.Country;
import com.lapsa.international.phone.PhoneNumber;
import com.lapsa.kz.country.KZCity;
import com.lapsa.kz.economic.KZEconomicSector;

import tech.lapsa.esbd.dao.elements.dict.CountryService.CountryServiceLocal;
import tech.lapsa.esbd.dao.elements.dict.KZCityService.KZCityServiceLocal;
import tech.lapsa.esbd.dao.elements.dict.KZEconomicSectorService.KZEconomicSectorServiceLocal;
import tech.lapsa.esbd.dao.entities.complex.SubjectEntity;
import tech.lapsa.esbd.dao.entities.complex.SubjectEntity.SubjectEntityBuilder;
import tech.lapsa.esbd.dao.entities.embeded.ContactInfo;
import tech.lapsa.esbd.dao.entities.embeded.OriginInfo;
import tech.lapsa.esbd.jaxws.wsimport.Client;
import tech.lapsa.java.commons.function.MyOptionals;
import tech.lapsa.kz.taxpayer.TaxpayerNumber;

public class ASubjectEntityEsdbdConverter {

    @EJB
    private CountryServiceLocal countries;

    @EJB
    private KZEconomicSectorServiceLocal economicSectors;

    @EJB
    private KZCityServiceLocal cities;

    void fillValues(final Client source, final SubjectEntityBuilder<?, ?> builder) throws IllegalArgumentException {
	final int id = source.getID();

	{
	    // ID s:int Идентификатор клиента (обязательно)
	    MyOptionals.of(id)
		    .ifPresent(builder::withId);
	}

	{
	    // COUNTRY_ID s:int Страна (справочник COUNTRIES)
	    // SETTLEMENT_ID s:int Населенный пункт (справочник SETTLEMENTS)
	    OriginInfo.builder() //
		    .withCountry(optField(SubjectEntity.class,
			    id,
			    countries::getById,
			    "origin.country",
			    Country.class,
			    MyOptionals.of(source.getCOUNTRYID())))
		    .withCity(optField(SubjectEntity.class,
			    id,
			    cities::getById,
			    "origin.city",
			    KZCity.class,
			    MyOptionals.of(source.getSETTLEMENTID())))
		    .buildTo(builder::withOrigin);
	}

	{
	    // PHONES s:string Номера телефонов
	    // EMAIL s:string Адрес электронной почты
	    // Address s:string Адрес
	    // WWW s:string Сайт
	    ContactInfo.builder() //
		    .withPhone(MyOptionals.of(source.getPHONES())
			    .map(PhoneNumber::assertValid)) //
		    .withHomeAdress(MyOptionals.of(source.getAddress())) //
		    .withEmail(MyOptionals.of(source.getEMAIL())) //
		    .withSiteUrl(MyOptionals.of(source.getWWW()))
		    .buildTo(builder::withContact);
	}

	{
	    // TPRN s:string РНН
	    MyOptionals.of(source.getTPRN())
		    .ifPresent(builder::withTaxPayerNumber);
	}

	{
	    // DESCRIPTION s:string Примечание
	    MyOptionals.of(source.getDESCRIPTION())
		    .ifPresent(builder::withComments);
	}

	{
	    // RESIDENT_BOOL s:int Признак резидентства (обязательно)
	    builder.withResident(Boolean.valueOf(source.getRESIDENTBOOL() == 1));
	}

	{
	    // IIN s:string ИИН/БИН
	    MyOptionals.of(source.getIIN())
		    .map(TaxpayerNumber::assertValid)
		    .ifPresent(builder::withIdNumber);
	}

	{
	    // ECONOMICS_SECTOR_ID s:int Сектор экономики (справочник
	    // ECONOMICS_SECTORS)
	    optField(SubjectEntity.class,
		    id,
		    economicSectors::getById,
		    "EconomicsSector",
		    KZEconomicSector.class,
		    MyOptionals.of(source.getECONOMICSSECTORID()))
			    .ifPresent(builder::withEconomicsSector);
	}
    }
}
