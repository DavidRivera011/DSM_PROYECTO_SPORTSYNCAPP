
@file:Suppress(
  "KotlinRedundantDiagnosticSuppress",
  "PropertyName",
  "MayBeConstant",
  "RedundantVisibilityModifier",
  "RedundantCompanionReference",
  "RemoveEmptyClassBody",
  "SpellCheckingInspection",
  "unused",
)

package com.example.sportsyncapp.connector


import kotlinx.coroutines.flow.filterNotNull as _flow_filterNotNull
import kotlinx.coroutines.flow.map as _flow_map


public interface GetInstitucionesQuery :
    com.google.firebase.dataconnect.generated.GeneratedQuery<
      DefaultConnector,
      GetInstitucionesQuery.Data,
      Unit
    >
{
  

  
    @kotlinx.serialization.Serializable
  public data class Data(
  
    val institucioness: List<InstitucionessItem>,
  
  ) {
    
      
        @kotlinx.serialization.Serializable
  public data class InstitucionessItem(
  
    val id: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
    val nombre: String,
  
    val logo: String?,
  
    val descripcion: String?,
  
  ) {
    
    
  }
      
    
    
  }
  

  public companion object {
    public val operationName: String = "GetInstituciones"

    public val dataDeserializer: kotlinx.serialization.DeserializationStrategy<Data> =
      kotlinx.serialization.serializer()

    public val variablesSerializer: kotlinx.serialization.SerializationStrategy<Unit> =
      kotlinx.serialization.serializer()
  }
}

public fun GetInstitucionesQuery.ref(
  
): com.google.firebase.dataconnect.QueryRef<
    GetInstitucionesQuery.Data,
    Unit
  > =
  ref(
    
      Unit
    
  )

public suspend fun GetInstitucionesQuery.execute(

  

  ): com.google.firebase.dataconnect.QueryResult<
    GetInstitucionesQuery.Data,
    Unit
  > =
  ref(
    
  ).execute()


  public fun GetInstitucionesQuery.flow(
    
    ): kotlinx.coroutines.flow.Flow<GetInstitucionesQuery.Data> =
    ref(
        
      ).subscribe()
      .flow
      ._flow_map { querySubscriptionResult -> querySubscriptionResult.result.getOrNull() }
      ._flow_filterNotNull()
      ._flow_map { it.data }

