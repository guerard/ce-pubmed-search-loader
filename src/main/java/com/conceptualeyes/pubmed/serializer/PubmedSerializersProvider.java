package com.conceptualeyes.pubmed.serializer;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.common.collect.ImmutableSet;
import com.conceptualeyes.pubmed.models.AccessionNumber;
import com.conceptualeyes.pubmed.models.AccessionNumberList;
import com.conceptualeyes.pubmed.models.ArticleIdList;
import com.conceptualeyes.pubmed.models.ChemicalList;
import com.conceptualeyes.pubmed.models.CitationSubset;
import com.conceptualeyes.pubmed.models.CollectiveName;
import com.conceptualeyes.pubmed.models.CommentsCorrectionsList;
import com.conceptualeyes.pubmed.models.ContractNumber;
import com.conceptualeyes.pubmed.models.DateCompleted;
import com.conceptualeyes.pubmed.models.DateRevised;
import com.conceptualeyes.pubmed.models.Day;
import com.conceptualeyes.pubmed.models.EndPage;
import com.conceptualeyes.pubmed.models.ForeName;
import com.conceptualeyes.pubmed.models.GeneSymbol;
import com.conceptualeyes.pubmed.models.GeneSymbolList;
import com.conceptualeyes.pubmed.models.Initials;
import com.conceptualeyes.pubmed.models.InvestigatorList;
import com.conceptualeyes.pubmed.models.Isbn;
import com.conceptualeyes.pubmed.models.Item;
import com.conceptualeyes.pubmed.models.Language;
import com.conceptualeyes.pubmed.models.LastName;
import com.conceptualeyes.pubmed.models.MedlineDate;
import com.conceptualeyes.pubmed.models.MedlinePgn;
import com.conceptualeyes.pubmed.models.MeshHeadingList;
import com.conceptualeyes.pubmed.models.Month;
import com.conceptualeyes.pubmed.models.Pagination;
import com.conceptualeyes.pubmed.models.PersonalNameSubjectList;
import com.conceptualeyes.pubmed.models.PubDate;
import com.conceptualeyes.pubmed.models.PublicationTypeList;
import com.conceptualeyes.pubmed.models.Season;
import com.conceptualeyes.pubmed.models.SpaceFlightMission;
import com.conceptualeyes.pubmed.models.StartPage;
import com.conceptualeyes.pubmed.models.Suffix;
import com.conceptualeyes.pubmed.models.SupplMeshList;
import com.conceptualeyes.pubmed.models.Year;
import com.conceptualeyes.pubmed.serializer.DateSerializerFactory.DatePartsAccessor;

import java.util.Set;

public class PubmedSerializersProvider {

    private static final Set<Class> SINGLE_VALUE_CLASSES =
            ImmutableSet.of(
                    AccessionNumber.class,
                    CitationSubset.class,
                    CollectiveName.class,
                    ContractNumber.class,
                    Day.class,
                    EndPage.class,
                    ForeName.class,
                    GeneSymbol.class,
                    Initials.class,
                    Isbn.class,
                    Item.class,
                    Language.class,
                    LastName.class,
                    MedlineDate.class,
                    MedlinePgn.class,
                    Month.class,
                    Season.class,
                    SpaceFlightMission.class,
                    StartPage.class,
                    Suffix.class,
                    Year.class);

    private static final Set<Class> SINGLE_DISTINCT_ELEMENTS_LIST_CLASSES =
            ImmutableSet.of(
                    Pagination.class,
                    PubDate.class);

    private static final Set<Class> SINGLE_LIST_CLASSES =
            ImmutableSet.of(
                    AccessionNumberList.class,
                    ArticleIdList.class,
                    ChemicalList.class,
                    CommentsCorrectionsList.class,
                    GeneSymbolList.class,
                    InvestigatorList.class,
                    MeshHeadingList.class,
                    PersonalNameSubjectList.class,
                    PublicationTypeList.class,
                    SupplMeshList.class);

    public static void addSerializers(SimpleModule simpleModule) {
        for (Class clazz : SINGLE_VALUE_CLASSES) {
            simpleModule.addSerializer(clazz, SingleValueSerializer.instance);
        }
        for (Class clazz : SINGLE_DISTINCT_ELEMENTS_LIST_CLASSES) {
            simpleModule.addSerializer(clazz, SingleDistinctElementsListGetterSerializer.instance);
        }
        for (Class clazz : SINGLE_LIST_CLASSES) {
            simpleModule.addSerializer(clazz, ListClassSerializer.instance);
        }
        DateSerializerFactory factory = DateSerializerFactory.instance;
        simpleModule.addSerializer(
                DateCompleted.class,
                factory.newSerializer(new DateCompletedDatePartsAccessor()));
        simpleModule.addSerializer(
                DateRevised.class,
                factory.newSerializer(new DateRevisedDatePartsAccessor()));
    }

    private static class DateCompletedDatePartsAccessor implements DatePartsAccessor<DateCompleted> {
        @Override
        public String getYear(DateCompleted object) {
            return object.getYear().getvalue();
        }

        @Override
        public String getMonth(DateCompleted object) {
            return object.getMonth().getvalue();
        }

        @Override
        public String getDay(DateCompleted object) {
            return object.getDay().getvalue();
        }
    }

    private static class DateRevisedDatePartsAccessor implements DatePartsAccessor<DateRevised> {
        @Override
        public String getYear(DateRevised object) {
            return object.getYear().getvalue();
        }

        @Override
        public String getMonth(DateRevised object) {
            return object.getMonth().getvalue();
        }

        @Override
        public String getDay(DateRevised object) {
            return object.getDay().getvalue();
        }
    }
}
