
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


public interface GetEstadosInscripcionQuery :
    com.google.firebase.dataconnect.generated.GeneratedQuery<
      DefaultConnector,
      GetEstadosInscripcionQuery.Data,
      Unit
    >
{
  

  
    @kotlinx.serialization.Serializable
  public data class Data(
  
    val estadosInscripcioness: List<EstadosInscripcionessItem>,
  
  ) {
    
      
        @kotlinx.serialization.Serializable
  public data class EstadosInscripcionessItem(
  
    val id: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
    val nombreEstado: String?,
  
  ) {
    
    
  }
      
    
    
  }
  

  public companion object {
    public val operationName: String = "GetEstadosInscripcion"

    public val dataDeserializer: kotlinx.serialization.DeserializationStrategy<Data> =
      kotlinx.serialization.serializer()

    public val variablesSerializer: kotlinx.serialization.SerializationStrategy<Unit> =
      kotlinx.serialization.serializer()
  }
}

public fun GetEstadosInscripcionQuery.ref(
  
): com.google.firebase.dataconnect.QueryRef<
    GetEstadosInscripcionQuery.Data,
    Unit
  > =
  ref(
    
      Unit
    
  )

public suspend fun GetEstadosInscripcionQuery.execute(

  

  ): com.google.firebase.dataconnect.QueryResult<
    GetEstadosInscripcionQuery.Data,
    Unit
  > =
  ref(
    
  ).execute()


  public fun GetEstadosInscripcionQuery.flow(
    
    ): kotlinx.coroutines.flow.Flow<GetEstadosInscripcionQuery.Data> =
    ref(
        
      ).subscribe()
      .flow
      ._flow_map { querySubscriptionResult -> querySubscriptionResult.result.getOrNull() }
      ._flow_filterNotNull()
      ._flow_map { it.data }

