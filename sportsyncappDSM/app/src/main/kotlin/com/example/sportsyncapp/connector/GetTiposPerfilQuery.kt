
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


public interface GetTiposPerfilQuery :
    com.google.firebase.dataconnect.generated.GeneratedQuery<
      DefaultConnector,
      GetTiposPerfilQuery.Data,
      Unit
    >
{
  

  
    @kotlinx.serialization.Serializable
  public data class Data(
  
    val tipoPerfils: List<TipoPerfilsItem>,
  
  ) {
    
      
        @kotlinx.serialization.Serializable
  public data class TipoPerfilsItem(
  
    val id: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
    val nombrePerfil: String,
  
  ) {
    
    
  }
      
    
    
  }
  

  public companion object {
    public val operationName: String = "GetTiposPerfil"

    public val dataDeserializer: kotlinx.serialization.DeserializationStrategy<Data> =
      kotlinx.serialization.serializer()

    public val variablesSerializer: kotlinx.serialization.SerializationStrategy<Unit> =
      kotlinx.serialization.serializer()
  }
}

public fun GetTiposPerfilQuery.ref(
  
): com.google.firebase.dataconnect.QueryRef<
    GetTiposPerfilQuery.Data,
    Unit
  > =
  ref(
    
      Unit
    
  )

public suspend fun GetTiposPerfilQuery.execute(

  

  ): com.google.firebase.dataconnect.QueryResult<
    GetTiposPerfilQuery.Data,
    Unit
  > =
  ref(
    
  ).execute()


  public fun GetTiposPerfilQuery.flow(
    
    ): kotlinx.coroutines.flow.Flow<GetTiposPerfilQuery.Data> =
    ref(
        
      ).subscribe()
      .flow
      ._flow_map { querySubscriptionResult -> querySubscriptionResult.result.getOrNull() }
      ._flow_filterNotNull()
      ._flow_map { it.data }

